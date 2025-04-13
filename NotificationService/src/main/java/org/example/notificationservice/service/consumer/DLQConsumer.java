package org.example.notificationservice.service.consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DLQConsumer {
    @RabbitListener(queues = "${rabbitmq.queues.dlx-email}")
    public void consume(String message) {
        log.error("❌ failed to send email {}", message);
        //Logic to handle unresolved message ...
    }
}
