package com.example.Backend.service;

import com.example.Backend.dto.Request.CommentRequest;
import com.example.Backend.dto.Response.CommentResponse;

public interface CommentService {
    CommentResponse addComment(Long blogId, CommentRequest commentRequest, String name);

    CommentResponse editComment(Long commentId, CommentRequest commentRequest, String name);

    String deleteComment(Long commentId, String name);
}
