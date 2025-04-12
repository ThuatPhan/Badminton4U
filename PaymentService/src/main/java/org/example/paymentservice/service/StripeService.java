package org.example.paymentservice.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.dto.request.PaymentCompletedEvent;
import org.example.paymentservice.dto.response.OrderResponse;
import org.example.paymentservice.dto.request.PaymentRequest;
import org.example.paymentservice.dto.response.ProductResponse;
import org.example.paymentservice.service.producer.PaymentEventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StripeService {
    @NonFinal
    @Value("${stripe.success-url}")
    String successUrl;

    @NonFinal
    @Value("${stripe.cancel-url}")
    String cancelUrl;

    @NonFinal
    @Value("${stripe.currency}")
    String currency;

    @NonFinal
    @Value("${stripe.webhook-secret}")
    String webhookSecret;

    PaymentEventProducer paymentEventProducer;

    public String createCheckoutSession(PaymentRequest paymentRequest) {
        OrderResponse order = paymentRequest.getOrder();

        List<SessionCreateParams.LineItem> lineItems = order.getItems().stream().map(item -> {
            ProductResponse product = item.getProduct();
            long unitAmount = currency.equals("usd") ? Math.round(item.getPrice() * 100) : Math.round(item.getPrice());

            return SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.getQuantity())
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(currency)
                                    .setUnitAmount(unitAmount)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(product.getName())
                                                    .addImage(product.getImage())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
        }).toList();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .putMetadata("order_id", order.getId())
                .putMetadata("email", paymentRequest.getEmail())
                .addAllLineItem(lineItems)
                .build();

        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            log.error("❌ Failed to create checkout session: {}", e.getMessage());
            throw new RuntimeException("Failed to create checkout session");
        }
        log.info("✅ Checkout session created: {}", session.getId());
        return session.getUrl();
    }

    public ResponseEntity<String> handleWebhook(HttpServletRequest request) {
        String sigHeader = request.getHeader("Stripe-Signature");
        byte[] payloadBytes;

        try {
            payloadBytes = request.getInputStream().readAllBytes();
        } catch (IOException e) {
            log.error("❌ Failed to read request body: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        }

        Event event;
        try {
            event = Webhook.constructEvent(new String(payloadBytes, StandardCharsets.UTF_8), sigHeader, webhookSecret);
        } catch (Exception e) {
            log.error("❌ Webhook signature verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook signature verification failed");
        }

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = deserializer.getObject().orElse(null);

        if (stripeObject == null) {
            log.warn("⚠️ Unable to parse Stripe object from event");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to parse Stripe object");
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> {
                Session session = (Session) stripeObject;
                paymentEventProducer.sendPaymentCompletedEvent(PaymentCompletedEvent.builder()
                        .orderId(session.getMetadata().get("order_id"))
                        .email(session.getMetadata().get("email"))
                        .build());
            }
            case "payment_intent.succeeded" -> {
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                log.info("✅ Payment intent succeeded: {}", paymentIntent.getId());
            }
            default -> log.warn("⚠️ Unhandled event type: {}", event.getType());
        }

        return ResponseEntity.ok().build();
    }
}


