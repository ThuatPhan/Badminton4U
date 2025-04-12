package org.example.orderservice.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitConfig {
    @Value("${rabbitmq.exchange}")
    String exchange;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange, true, false);
    }
}

