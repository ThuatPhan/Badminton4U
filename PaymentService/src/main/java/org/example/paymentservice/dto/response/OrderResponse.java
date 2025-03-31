package org.example.paymentservice.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderResponse {
    String id;
    List<OrderItemResponse> items;
    Double totalAmount;
    OrderStatus status;
    LocalDateTime createdAt;
}
