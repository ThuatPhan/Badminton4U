package org.example.notificationservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMailEvent {
    String to;
    String subject;
    OrderResponse order;
}
