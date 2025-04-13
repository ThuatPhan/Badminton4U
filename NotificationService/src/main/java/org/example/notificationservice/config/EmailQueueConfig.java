package org.example.notificationservice.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailQueueConfig {
    @Value("${rabbitmq.queues.email}")
    String emailQueue;

    @Value("${rabbitmq.routing-keys.email}")
    String emailRoutingKey;

    @Value("${rabbitmq.routing-keys.dlx-email}")
    String deadLetterRoutingKey;

    @Value("${rabbitmq.exchanges.dlx-exchange}")
    String deadLetterExchangeName;

    final TopicExchange orderExchange;

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(emailQueue)
                .withArgument("x-dead-letter-exchange", deadLetterExchangeName)
                .withArgument("x-dead-letter-routing-key", deadLetterRoutingKey)
                .build();
    }

    @Bean
    public Binding emailBinding(Queue emailQueue) {
        return BindingBuilder.bind(emailQueue)
                .to(orderExchange)
                .with(emailRoutingKey);
    }
}
