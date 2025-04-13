package org.example.notificationservice.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.dto.SendMailEvent;
import org.example.notificationservice.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderEventConsumer {
    EmailService emailService;
    ObjectMapper objectMapper;

    @RabbitListener(queues = "${rabbitmq.queues.email}", containerFactory = "rabbitListenerContainerFactory")
    public void sendEmail(String request) {
        try {
            SendMailEvent event = objectMapper.readValue(request, SendMailEvent.class);
            emailService.sendEmail(event);
            log.info("✅ Email sent to: {}", event.getTo());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse email event JSON");
        }
    }
}
