package org.example.notificationservice.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitConfig {
    @Value("${rabbitmq.exchanges.order}")
    String orderExchange;

    @Value("${rabbitmq.exchanges.dlx-exchange}")
    String deadLetterExchange;

    @Bean(name = "orderExchange")
    public TopicExchange orderExchange() {
        return new TopicExchange(orderExchange, true, false);
    }

    @Bean(name = "deadLetterExchange")
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(deadLetterExchange, true, false);
    }
}
