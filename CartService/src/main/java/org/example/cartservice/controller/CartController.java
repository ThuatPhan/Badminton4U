package org.example.cartservice.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.cartservice.dto.request.CartItemRequest;
import org.example.cartservice.dto.request.SyncCartItemRequest;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.PagedResponse;
import org.example.cartservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "Cart Management",
        description = "Endpoints for managing shopping carts, including adding, updating, and removing items."
)
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemResponse addCartItems(@AuthenticationPrincipal Jwt jwt,
                                         @Valid @RequestBody CartItemRequest request) {
        return cartService.addCartItem(jwt.getSubject(), request);
    }

    @GetMapping
    public PagedResponse<CartItemResponse> getCartItems(@AuthenticationPrincipal Jwt jwt,
                                                        @RequestParam int page,
                                                        @RequestParam int size
    ) {
        return cartService.getCartItems(jwt.getSubject(), page, size);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void syncCartItems(@AuthenticationPrincipal Jwt jwt,
                              @Valid @RequestBody SyncCartItemRequest request) {
        cartService.syncCartItems(jwt.getSubject(), request);
    }
}
