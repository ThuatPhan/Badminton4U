package org.example.paymentservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.paymentservice.dto.response.OrderResponse;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentRequest {
    @NotNull(message = "email is required")
    @Email(message = "not valid email format")
    String email;

    @NotNull(message = "order is required")
    @Valid
    OrderResponse order;
}
