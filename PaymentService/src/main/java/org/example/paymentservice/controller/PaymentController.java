package org.example.paymentservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.dto.request.PaymentRequest;
import org.example.paymentservice.service.StripeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    StripeService stripeService;

    @PostMapping("/internal/checkout")
    public String createCheckoutSession(@Valid @RequestBody PaymentRequest request) {
        return stripeService.createCheckoutSession(request);
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) {
        return stripeService.handleWebhook(request);
    }

}
