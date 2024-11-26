package com.example.Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String status; // pending, accepted, rejected, blocked, follow
}
