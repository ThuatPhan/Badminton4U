package org.example.paymentservice.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaymentCompletedEvent {
    String orderId;
    String email;
}
