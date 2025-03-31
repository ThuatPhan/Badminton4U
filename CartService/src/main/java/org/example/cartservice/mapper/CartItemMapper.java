package org.example.cartservice.mapper;

import org.example.cartservice.dto.request.CartItemRequest;
import org.example.cartservice.dto.response.CartItemResponse;
import org.example.cartservice.dto.response.ProductResponse;
import org.example.cartservice.entity.Cart;
import org.example.cartservice.entity.CartItem;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(target = "cart", expression = "java(cart)")
    CartItem fromRequest(CartItemRequest request, @Context Cart cart);

    @Mapping(target = "id", expression = "java(cartItem.getId())")
    @Mapping(target = "product", expression = "java(product)")
    CartItemResponse toResponse(CartItem cartItem, @Context ProductResponse product);
}
