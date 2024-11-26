package com.example.Backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
//Comment: id, Blog_id, user_id, content, created_at
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blog_id",
            referencedColumnName = "id")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private User user;

    @NotBlank(message = "Content is required")
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
