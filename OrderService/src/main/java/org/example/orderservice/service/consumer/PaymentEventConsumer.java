package org.example.orderservice.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dto.request.PaymentCompletedEvent;
import org.example.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEventConsumer {
    OrderService orderService;
    ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbitmq.queues.payment}")
    public void updateOrderStatus(String message) {
        try {
            PaymentCompletedEvent event = objectMapper.readValue(message, PaymentCompletedEvent.class);
            orderService.updateOrderStatus(event);
            log.info("✅ Status of order {} updated to", event.getOrderId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse payment event JSON");
        }
    }
}
