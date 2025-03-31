package org.example.orderservice.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.orderservice.dto.request.OrderRequest;
import org.example.orderservice.dto.response.ApiResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> createOrder(@AuthenticationPrincipal Jwt jwt,
                                                  @Valid @RequestBody OrderRequest orderRequest) {
        return ApiResponse.success(orderService.createOrder(jwt.getSubject(), orderRequest),
                "Order created successfully");
    }

    @PostMapping("/checkout")
    public ApiResponse<String> createCheckoutSession(@AuthenticationPrincipal Jwt jwt,
                                                     @Valid @RequestBody OrderRequest orderRequest) {
        return ApiResponse.success(orderService.createCheckoutSession(jwt.getSubject(), orderRequest),
                "Checkout session created successfully");
    }
}
