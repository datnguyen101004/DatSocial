package com.example.Backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponseDto {
    private String title;
    private String content;
    private String author;
    private int likesCount;
    private int commentsCount;
    private int sharesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
