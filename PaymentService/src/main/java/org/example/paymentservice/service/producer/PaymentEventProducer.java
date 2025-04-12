package org.example.paymentservice.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.dto.request.PaymentCompletedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentEventProducer {
    RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;

    @NonFinal
    @Value("${rabbitmq.exchange}")
    String exchange;

    @NonFinal
    @Value("${rabbitmq.routing-keys.payment-completed}")
    String routingKey;

    public void sendPaymentCompletedEvent(PaymentCompletedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("✅ Sent payment completed event to exchange: {} with routingKey: {}", exchange, routingKey);
        } catch (JsonProcessingException e) {
            log.info("❌ Failed to serialize payment completed event");
            throw new RuntimeException("Failed to serialize payment completed event");
        }
        log.info("Sent payment event to queue");
    }
}
