package com.example.Backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.queue_name}")
    private String QUEUE;

    @Value("${rabbitmq.exchange_name}")
    private String EXCHANGE;

    @Value("${rabbitmq.routing_key}")
    private String ROUTING_KEY;

    public String createRoutingKey(String name) {
        return ROUTING_KEY + "_" + name;
    }

    @Bean
    public org.springframework.amqp.core.Queue createQueue() {
        return new org.springframework.amqp.core.Queue(QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    public Binding createBinding(Queue queue, DirectExchange exchange, String routingKey) {
        return  BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKey);
    }
}
