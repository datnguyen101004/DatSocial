package com.example.Backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
//Blog: id, Title, content, author_id,created_at
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private User author;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Share> shares;
}
