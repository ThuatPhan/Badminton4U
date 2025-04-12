package org.example.orderservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaymentCompletedEvent {
    String orderId;
    String email;
}
