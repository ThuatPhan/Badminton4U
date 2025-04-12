package org.example.cartservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cartservice.client.ProductServiceClient;
import org.example.cartservice.dto.request.CartItemRequest;
import org.example.cartservice.dto.request.SyncCartItemRequest;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.PagedResponse;
import org.example.cartservice.dto.response.ProductResponse;
import org.example.cartservice.entity.Cart;
import org.example.cartservice.entity.CartItem;
import org.example.cartservice.exception.AppException;
import org.example.cartservice.exception.ErrorCode;
import org.example.cartservice.mapper.CartItemMapper;
import org.example.cartservice.repository.CartItemRepository;
import org.example.cartservice.repository.CartRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    ProductServiceClient productServiceClient;
    CartItemRepository cartItemRepository;
    CartItemMapper cartItemMapper;

    @CacheEvict(value = "cart", allEntries = true)
    public CartItemResponse addCartItem(String userId, CartItemRequest request) {
        ProductResponse product = productServiceClient.getProduct(request.getProductId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> Cart.builder()
                        .userId(userId)
                        .build());

        Optional<CartItem> existingItemOpt = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), request.getProductId());

        CartItem cartItem;

        if (existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = cartItemMapper.fromRequest(request, cart);
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);
        return cartItemMapper.toResponse(cartItem, product);
    }

    @Cacheable(value = "cart", key = "'cart::user::' +#userId+ '::items::page::' +#page+ '::size::' +#size")
    public PagedResponse<CartItemResponse> getCartItems(String userId, int page, int size) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

        Page<CartItem> pageResult = cartItemRepository
                .findAllByCart(cart, PageRequest.of(page - 1, size));

        List<String> productIds = pageResult.getContent().stream()
                .map(CartItem::getProductId).toList();

        List<ProductResponse> productResponses = getProductByIds(productIds);

        // For product lookup with O(1) complexity
        Map<String, ProductResponse> productMap = productResponses.stream()
                .collect(Collectors.toMap(ProductResponse::getId, Function.identity()));

        List<CartItemResponse> cartItems = pageResult.getContent().stream().map((cartItem) -> {
            if (!productMap.containsKey(cartItem.getProductId())) {
                throw new AppException(ErrorCode.PRODUCT_NOT_EXIST);
            }
            return cartItemMapper.toResponse(cartItem, productMap.get(cartItem.getProductId()));
        }).toList();

        return PagedResponse.<CartItemResponse>builder()
                .totalItems(pageResult.getTotalElements())
                .items(cartItems)
                .build();
    }

    @CacheEvict(value = "cart", allEntries = true)
    public void syncCartItems(String userId, SyncCartItemRequest request) {
        List<CartItemRequest> updatedItems = request.getItems();

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));

        List<String> productIds = updatedItems.stream()
                .map(CartItemRequest::getProductId)
                .toList();

        List<ProductResponse> productResponses = getProductByIds(productIds);

        // Map productId to ProductResponse for fast lookup (O(1))
        Map<String, ProductResponse> productMap = productResponses.stream()
                .collect(Collectors.toMap(ProductResponse::getId, Function.identity()));

        List<String> invalidIds = productIds.stream()
                .filter(id -> !productMap.containsKey(id))
                .toList();

        if (!invalidIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXIST);
        }

        // Map existing cart items for quick access
        Map<String, CartItem> existingItemsMap = cart.getCartItems().stream()
                .collect(Collectors.toMap(CartItem::getProductId, Function.identity()));

        // Add or update items
        for (CartItemRequest itemReq : updatedItems) {
            CartItem existingItem = existingItemsMap.get(itemReq.getProductId());

            if (existingItem != null) {
                existingItem.setQuantity(itemReq.getQuantity());
            } else {
                cart.getCartItems().add(cartItemMapper.fromRequest(itemReq, cart));
            }
        }

        // Remove items not present in the updated list
        Set<String> updatedProductIdSet = new HashSet<>(productIds);
        cart.getCartItems().removeIf(item ->
                !updatedProductIdSet.contains(item.getProductId()));

        cartRepository.save(cart);
    }

    private List<ProductResponse> getProductByIds(List<String> productIds) {
        return productServiceClient.getProducts(productIds);
    }
}

