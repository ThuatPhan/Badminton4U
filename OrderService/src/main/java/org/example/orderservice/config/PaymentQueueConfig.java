package org.example.orderservice.config;

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
public class PaymentQueueConfig {
    @NonFinal
    @Value("${rabbitmq.queues.payment}")
    String queue;

    @NonFinal
    @Value("${rabbitmq.routing-keys.payment}")
    String routingKey;

    TopicExchange exchange;

    @Bean
    public Queue paymentQueue() {
        return new Queue(queue, true);
    }

    @Bean
    public Binding paymentBinding(Queue paymentQueue) {
        return BindingBuilder.bind(paymentQueue).to(exchange).with(routingKey);
    }
}
