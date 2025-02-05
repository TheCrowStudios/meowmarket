package com.thecrowstudios.meowmarket.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.events.V1BillingMeterErrorReportTriggeredEvent.EventData;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.LineItem;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.model.terminal.Reader.Action.SetReaderDisplay.Cart;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection.AllowedCountry;
import com.thecrowstudios.meowmarket.authentication.User;
import com.thecrowstudios.meowmarket.authentication.UserService;
import com.thecrowstudios.meowmarket.cart.CartItem;
import com.thecrowstudios.meowmarket.cart.CartService;
import com.thecrowstudios.meowmarket.listings.Listing;
import com.thecrowstudios.meowmarket.listings.ListingRepository;
import com.thecrowstudios.meowmarket.orders.Order;
import com.thecrowstudios.meowmarket.orders.OrderListing;
import com.thecrowstudios.meowmarket.orders.OrderRepository;

@Controller
@RequestMapping("/api/stripe")
public class StripeController {
    @Value("${stripe.apikey}")
    private String stripeApiKey;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/create-checkout-session/listing/{listingId}")
    public ResponseEntity<Map<String, String>> createCheckoutSessionListing(@PathVariable Integer listingId) {
        Listing listing;

        try {
            listing = listingRepository.findById(listingId).orElseThrow();
        } catch (Exception e) {
            System.out.println("[ERROR] could not find listing");
            return ResponseEntity.badRequest().body(null);
        }

        Stripe.apiKey = stripeApiKey;

        SessionCreateParams.Builder builder = createDefaultStripeParams();

        Integer userId = 0; // default id to add to the metadata if not logged in
        User user = userService.getUser();

        if (user != null) {
            userId = user.getId();
        }

        SessionCreateParams params = builder
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder().setCurrency("gbp")
                                                .setUnitAmountDecimal(new BigDecimal(listing.getPrice() * 100)
                                                        .setScale(2, RoundingMode.HALF_UP))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(listing.getTitle())
                                                                .putMetadata("listingId", listing.getId().toString())
                                                                .putMetadata("userId", userId.toString())
                                                                .build())
                                                .build())
                                .build())
                .build();

        if (user != null) {
            builder.setCustomerEmail(user.getEmail());
        }

        try {
            Session session = Session.create(params);
            Map<String, String> map = new HashMap();
            map.put("clientSecret", session.getClientSecret());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            System.out.println("[ERROR] could not create stripe checkout session");
            System.out.println(e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/checkout/listing/{listingId}")
    public String getListingCheckout(@PathVariable Integer listingId, Model model) {
        model.addAttribute("path", "/api/stripe/create-checkout-session/listing/" + listingId);
        return ("checkout");
    }

    @PostMapping("/checkout/cart")
    public String getCartCheckout(Model model) {
        model.addAttribute("path", "/api/stripe/create-checkout-session/cart");
        return ("checkout");
    }

    @PostMapping("/create-checkout-session/cart")
    public ResponseEntity<Map<String, String>> createCheckoutSessionCart() {
        List<CartItem> cartItems = cartService.getCartItems();

        if (cartItems.size() == 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Stripe.apiKey = stripeApiKey;

        SessionCreateParams.Builder builder = createDefaultStripeParams();

        Integer userId = 0; // default id to add to the metadata if not logged in
        User user = userService.getUser();

        if (user != null) {
            userId = user.getId();
        }

        for (CartItem cartItem : cartItems) {
            builder
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(cartItem.getQuantity().longValue())
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder().setCurrency("gbp")
                                                    .setUnitAmountDecimal(
                                                            new BigDecimal(cartItem.getListing().getPrice() * 100)
                                                                    .setScale(2, RoundingMode.HALF_UP))
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(cartItem.getListing().getTitle())
                                                                    .putMetadata("listingId",
                                                                            cartItem.getListing().getId().toString())
                                                                    .putMetadata("userId", userId.toString())
                                                                    .build())
                                                    .build())
                                    .build());
        }

        if (user != null) {
            builder.setCustomerEmail(user.getEmail());
        }

        SessionCreateParams params = builder.build();

        try {
            Session session = Session.create(params);
            Map<String, String> map = new HashMap();
            map.put("clientSecret", session.getClientSecret());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            System.out.println("[ERROR] could not create stripe checkout session");
            System.out.println(e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/session-status/{session_id}")
    public ResponseEntity<Map<String, String>> getSessionStatus(@PathVariable String session_id) {
        Session session;

        try {
            session = Session.retrieve(session_id);
        } catch (Exception e) {
            System.out.println("[ERROR] could not retrieve session");
            System.out.println(e);
            return null;
        }

        Map<String, String> map = new HashMap();
        map.put("status", session.getStatus());
        map.put("customer_email", session.getCustomerDetails().getEmail());

        return ResponseEntity.ok(map);
    }

    @PostMapping("/hook")
    public ResponseEntity<String> stripeHook(@RequestBody String payload) {
        Event event = null;
        Gson gson = new Gson();

        try {
            event = gson.fromJson(payload, Event.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = deserializer.getObject().orElse(null);

        if (stripeObject == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                System.out.println("Payment intent succeeded: " + paymentIntent.getId());
                break;
            case "checkout.session.completed":
                Session session = (Session) stripeObject;
                System.out.println("Checkout session completed: " + session.getId());
                handleCheckoutSessionCompleted(session.getId());
                break;
            default:
                break;
        }
        return ResponseEntity.ok("ok");
    }

    public SessionCreateParams.Builder createDefaultStripeParams() {
        String domain = "http://localhost:8080/";

        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setReturnUrl(domain + "return?session_id={CHECKOUT_SESSION_ID}")
                .setShippingAddressCollection(ShippingAddressCollection.builder().addAllowedCountry(AllowedCountry.GB)
                        .addAllowedCountry(AllowedCountry.IE).addAllowedCountry(AllowedCountry.IM).build());

        return params;
    }

    private void handleCheckoutSessionCompleted(String sessionId) {
        Session session;
        SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("line_items.data.price.product")
                .build();

        try {
            session = Session.retrieve(sessionId, params, null);
        } catch (Exception e) {
            System.out.println("[ERROR] could not retrieve session");
            return;
        }

        List<LineItem> lineItems;

        try {
            lineItems = session.getLineItems().getData();
        } catch (Exception e) {
            System.out.println("[ERROR] could not list line items");
            return;
        }

        System.out.println("[OK] sold items: " + lineItems.size());

        User user = null; // userService.getUser() wont work because the hook is called
                          // from a listener so we have to put it in the metadata of the order

        for (LineItem lineItem : lineItems) {
            Map<String, String> metadata = lineItem.getPrice().getProductObject().getMetadata();
            Integer listingId = Integer.parseInt(metadata.get("listingId"));
            Integer userId = Integer.parseInt(metadata.get("userId"));
            Listing listing = listingRepository.findById(listingId).orElse(null);

            if (user == null) {
                user = userService.getUserById(userId);
            }

            if (listing != null) {
                System.out.println("Sold: " + listing.getTitle() + " id: " + listing.getId());
                Order order = new Order();
                order.getListings().add(new OrderListing(order, listing, lineItem.getQuantity().intValue(), lineItem.getAmountTotal().doubleValue())); // how
                System.out.println("listings in order: " + order.getListings().size());
                if (user != null) {
                    System.out.println("purchased by: " + user.getEmail());
                    order.setUser(user);
                }

                order.setEmail(session.getCustomerDetails().getEmail());
                order.setAddress(session.getShippingDetails().getAddress().getLine1());
                order.setAddress2(session.getShippingDetails().getAddress().getLine2());
                order.setCity(session.getShippingDetails().getAddress().getCity());
                order.setPostCode(session.getShippingDetails().getAddress().getPostalCode());

                Order savedOrder = orderRepository.save(order);
                System.out.println("listings in saved order: " + savedOrder.getListings().size());
            } else {
                System.out.println("[ERROR] could not find listing");
            }

            // TODO - add item to sold
        }
    }
}
