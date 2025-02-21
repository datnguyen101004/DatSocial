package com.example.Backend.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RabbitMqConfig {
    @Value("${rabbitmq.queue_name}")
    public String QUEUE;

    @Value("${rabbitmq.exchange_name}")
    public String EXCHANGE;

    @Bean
    public Queue createQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding createBinding(Queue queue, TopicExchange exchange) {
        return  BindingBuilder.bind(queue)
                .to(exchange)
                .with("blog.*");
    }
}
