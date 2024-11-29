package com.example.Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(
        name = "likes",  // Đổi tên bảng thành "likes"
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "blog_id"})
)
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "blog_id",
            referencedColumnName = "id")
    private Blog blog;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
