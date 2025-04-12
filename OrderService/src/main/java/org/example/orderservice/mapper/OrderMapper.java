package org.example.orderservice.mapper;

import org.example.orderservice.dto.request.OrderRequest;
import org.example.orderservice.dto.response.OrderItemResponse;
import org.example.orderservice.dto.response.OrderResponse;
import org.example.orderservice.entity.Order;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userId", expression = "java(userId)")
    @Mapping(target = "totalAmount", expression = "java(totalAmount)")
    Order fromRequest(OrderRequest request,
                      @Context String userId,
                      @Context Double totalAmount);

    @Mapping(target = "items", expression = "java(items)")
    OrderResponse toResponse(Order order,@Context List<OrderItemResponse> items);
}
