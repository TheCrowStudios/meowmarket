package com.thecrowstudios.meowmarket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Controller
@RequestMapping("/purchase")
public class StripeController {
    @PostMapping("/create-checkout-session")
    public String createCheckoutSession() {
        String domain = "http://localhost:8080/";
        Stripe.apiKey = "sk_test_51QHCnQJohaeZPxXhfGwW5Nt9qCfJD2BXCiJcy0iWJc5l1498zJrqtZDLqx43BbwBTub13ERnqDZvrykYa660Cxii00o0rlTTHM";
        SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(domain + "purchase/success")
                .setCancelUrl(domain + "purchase/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
                        .setPrice("price_1QjVP1DHTVEuSG0oMOVQzRHC").build())
                .build();

        try {
            Session session = Session.create(params);
            return "redirect:" + session.getUrl();
        } catch (Exception e) {
            return "redirect:/index";
        }
    }
}
