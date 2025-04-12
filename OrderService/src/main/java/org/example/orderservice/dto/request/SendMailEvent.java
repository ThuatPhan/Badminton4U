package org.example.orderservice.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.orderservice.dto.response.OrderResponse;

import java.io.Serializable;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMailEvent implements Serializable {
    String to;
    String subject;
    OrderResponse order;
}