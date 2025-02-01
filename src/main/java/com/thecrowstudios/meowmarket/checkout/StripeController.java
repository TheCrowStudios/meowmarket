package com.thecrowstudios.meowmarket.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.model.terminal.Reader.Action.SetReaderDisplay.Cart;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection;
import com.stripe.param.checkout.SessionCreateParams.ShippingAddressCollection.AllowedCountry;
import com.thecrowstudios.meowmarket.cart.CartItem;
import com.thecrowstudios.meowmarket.cart.CartService;
import com.thecrowstudios.meowmarket.listings.Listing;
import com.thecrowstudios.meowmarket.listings.ListingRepository;

@Controller
@RequestMapping("/api/create-checkout-session")
public class StripeController {
    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private CartService cartService;

    @PostMapping("/listing/{listingId}")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@PathVariable Integer listingId) {
        Listing listing;

        try {
            listing = listingRepository.findById(listingId).orElseThrow();
        } catch (Exception e) {
            System.out.println("[ERROR] could not find listing");
            return ResponseEntity.badRequest().body(null);
        }

        Stripe.apiKey = "sk_test_51QjVLQDHTVEuSG0oLYgw4BfQtSUDxoWcR66AZDTCs8CcvfdK7WikPEIuQyQnWL7MhkwXPA8j1P07CZwSJgG3PQZi00jKnTvJsA";

        SessionCreateParams.Builder builder = createDefaultStripeParams();

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
                                                                .build())
                                                .build())
                                .build())
                .build();

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
        model.addAttribute("path", "/api/create-checkout-session/listing/" + listingId);
        return ("checkout");
    }

    @PostMapping("/checkout/cart")
    public String getCartCheckout(Model model) {
        model.addAttribute("path", "/api/create-checkout-session/cart");
        return ("checkout");
    }

    @PostMapping("/cart")
    public ResponseEntity<Map<String, String>> createCheckoutSession() {
        List<CartItem> cartItems = cartService.getCartItems();

        if (cartItems.size() == 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Stripe.apiKey = "sk_test_51QjVLQDHTVEuSG0oLYgw4BfQtSUDxoWcR66AZDTCs8CcvfdK7WikPEIuQyQnWL7MhkwXPA8j1P07CZwSJgG3PQZi00jKnTvJsA";

        SessionCreateParams.Builder builder = createDefaultStripeParams();

        for (CartItem cartItem : cartItems) {
            // TODO - quantity
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
                                                                    .build())
                                                    .build())
                                    .build());
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
}
