package com.example.Backend.service.Impl;

import com.example.Backend.entity.Like;
import com.example.Backend.entity.User;
import com.example.Backend.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RedisTemplate<String, String> onlineUsersTemplate;
    private final BlogRepository blogRepository;

    private final static String ONLINE_USERS_KEY = "online_users";

    @RabbitListener(queues = "notification_queue")
    public void likeBlog(Message message) {
        //Get blogId and userId from message
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String blogId = routingKey.split("\\.")[1];
        String payload = new String(message.getBody());

        //get all user that online and follow blogId
        Set<Long> followUser = getFollower(blogId);

        //send message to all user that online and follow blogId
        for (Long id : followUser) {
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(id),"/queue/notification/", payload);
        }
    }

    private Set<Long> getFollower(String blogId) {
        Set<String> onlineUsers = onlineUsersTemplate.opsForSet().members(ONLINE_USERS_KEY);
        List<Like> likes = blogRepository.findById(Long.parseLong(blogId)).get().getLikes();
        return likes.stream()
                .filter(like -> onlineUsers.contains(like.getUser().getEmail()))
                .map(like -> like.getUser().getId())
                .collect(Collectors.toSet());
    }
}
