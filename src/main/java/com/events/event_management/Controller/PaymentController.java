package com.events.event_management.Controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPaymentIntent(@RequestParam int amount) throws StripeException {
        Stripe.apiKey = stripeSecretKey;  // Utilisation de la clé secrète Stripe

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) amount * 100)  // Montant en cents
                .setCurrency("usd")  // Devise
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }
}
