package org.example.orderservice.client;

import org.example.orderservice.config.FeignClientConfig;
import org.example.orderservice.dto.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "PAYMENT-SERVICE", path = "/api/payment", configuration = FeignClientConfig.class)
public interface PaymentServiceClient {
    @PostMapping("/internal/checkout")
    String createCheckoutSession(@RequestBody PaymentRequest request);
}
