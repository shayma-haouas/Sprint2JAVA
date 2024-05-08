package edu.esprit.flo.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;

public class Payment {
    public void processPayment(int amount) {
        try {
            Stripe.apiKey = "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";
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) amount * 100) // Amount should be in cents
                    .setCurrency("usd")
                    .addPaymentMethodType("card")
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);
            System.out.println("PaymentIntent created. Client Secret: " + intent.getClientSecret());
        } catch (StripeException e) {
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
    }
}


