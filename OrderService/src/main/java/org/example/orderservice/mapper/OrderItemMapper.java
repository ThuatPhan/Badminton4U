package org.example.orderservice.mapper;

import org.example.orderservice.dto.request.OrderItemRequest;
import org.example.orderservice.dto.response.OrderItemResponse;
import org.example.orderservice.dto.response.ProductResponse;
import org.example.orderservice.entity.OrderItem;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "price", expression = "java(product.getPrice())")
    OrderItem fromRequest(OrderItemRequest request, @Context ProductResponse product);

    @Mapping(target = "product", expression = "java(product)")
    OrderItemResponse toResponse(OrderItem orderItem, @Context ProductResponse product);
}
