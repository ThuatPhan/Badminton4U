package org.example.orderservice.service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dto.request.SendMailEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailEventProducer {
    RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;

    @NonFinal
    @Value("${rabbitmq.exchange}")
    String exchange;

    @NonFinal
    @Value("${rabbitmq.routing-keys.email}")
    String routingKey;

    public void sendConfirmationEmail(SendMailEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            log.info("✅ Sent mail event to exchange: {} with routingKey: {}", exchange, routingKey);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize send email event");
        }
    }
}
