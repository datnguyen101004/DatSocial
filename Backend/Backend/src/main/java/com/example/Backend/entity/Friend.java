package com.example.Backend.entity;

import com.example.Backend.entity.Enum.FriendStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id",
            referencedColumnName = "id")
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
