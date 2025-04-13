package org.example.notificationservice.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DLQQueueConfig {
    @Value("${rabbitmq.queues.dlx-email}")
    String deadLetterEmailQueue;

    @Value("${rabbitmq.routing-keys.dlx-email}")
    String deadLetterRoutingKey;

    final TopicExchange deadLetterExchange;

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(deadLetterEmailQueue, true);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(deadLetterRoutingKey);
    }
}
