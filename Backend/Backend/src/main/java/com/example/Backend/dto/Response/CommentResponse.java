package com.example.Backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long commentId;
    private String content;
    private Long userId;
    private String fullName;
    private Long blogId;
    private String createdAt;
    private String updatedAt;
}
