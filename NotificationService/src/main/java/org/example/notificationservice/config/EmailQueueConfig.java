package org.example.notificationservice.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailQueueConfig {
    @NonFinal
    @Value("${rabbitmq.queues.email}")
    String queue;

    @NonFinal
    @Value("${rabbitmq.routing-keys.email}")
    String routingKey;

    TopicExchange exchange;


    @Bean
    public Queue emailQueue() {
        return new Queue(queue, true);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue) {
        return BindingBuilder.bind(emailQueue).to(exchange).with(routingKey);
    }
}
