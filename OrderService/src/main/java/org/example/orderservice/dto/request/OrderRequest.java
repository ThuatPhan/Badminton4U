package org.example.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {
    @NotNull(message = "email is required")
    @Email(message = "not valid e-mail format")
    String email;

    @NotNull(message = "items is required")
    @Valid
    List<OrderItemRequest> items;
}
