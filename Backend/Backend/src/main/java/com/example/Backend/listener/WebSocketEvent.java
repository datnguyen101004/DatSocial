package com.example.Backend.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketEvent {

    /// Add user online and offline to Redis
    /// User online or offline -> event listener -> add or remove user to Redis

    private static final String ONLINE_USERS_KEY = "online_users";

    private final RedisTemplate<String, String> onlineUsersTemplate;

    @EventListener
    public void handleUserOnline(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String email = Objects.requireNonNull(accessor.getUser()).getName();
        onlineUsersTemplate.opsForSet().add(ONLINE_USERS_KEY, email);
        System.out.println("User connected: " + email);
    }

    @EventListener
    public void handleUserOffline(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String email = Objects.requireNonNull(accessor.getUser()).getName();
        onlineUsersTemplate.opsForSet().remove(ONLINE_USERS_KEY, email);
        System.out.println("User disconnected: " + email);
    }
}
