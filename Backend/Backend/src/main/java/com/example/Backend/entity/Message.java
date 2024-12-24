package com.example.Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id",
            referencedColumnName = "id", nullable = false)
    private User sender;
    private String content;
    private LocalDateTime sendAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "room_id",
            referencedColumnName = "id", nullable = false)
    private Room room;
}
