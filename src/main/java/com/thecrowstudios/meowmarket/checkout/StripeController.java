package com.thecrowstudios.meowmarket.checkout;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.thecrowstudios.meowmarket.listings.Listing;
import com.thecrowstudios.meowmarket.listings.ListingRepository;

@Controller
@RequestMapping("/purchase")
public class StripeController {
    @Autowired
    private ListingRepository listingRepository;

    @PostMapping("/create-checkout-session/{listingId}")
    public String createCheckoutSession(@PathVariable Integer listingId) {
        Listing listing;

        try {
            listing = listingRepository.findById(listingId).orElseThrow();
        } catch (Exception e) {
            System.out.println("[ERROR] could not find listing");
            return "redirect:/";
        }

        String domain = "http://localhost:8080/";
        Stripe.apiKey = "sk_test_51QHCnQJohaeZPxXhfGwW5Nt9qCfJD2BXCiJcy0iWJc5l1498zJrqtZDLqx43BbwBTub13ERnqDZvrykYa660Cxii00o0rlTTHM";
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(domain + "purchase/success")
                .setCancelUrl(domain + "purchase/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder().setCurrency("gbp")
                                                .setUnitAmountDecimal(new BigDecimal(listing.getPrice() * 100).setScale(2, RoundingMode.HALF_UP))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(listing.getTitle())
                                                                .build())
                                                .build())
                                .build())
                .build();

        try {
            Session session = Session.create(params);
            return "redirect:" + session.getUrl();
        } catch (Exception e) {
            System.out.println("[ERROR] could not create stripe checkout session");
            System.out.println(e);
            return "redirect:/";
        }
    }
}
