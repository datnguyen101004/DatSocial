package com.example.Backend.service.Impl;

import com.example.Backend.config.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqConfig rabbitMqConfig;

    public void likeBlog(String blogId, Long userId) {
        String routingKey = "blog." + blogId;
        String message = "User " + userId + " liked blog you follow";

        rabbitTemplate.convertAndSend(rabbitMqConfig.EXCHANGE, routingKey, message);
        System.out.println("sent message to rabbitmq");
    }
}
