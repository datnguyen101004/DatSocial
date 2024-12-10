package com.example.Backend.controller;

import com.example.Backend.dto.Request.CommentRequest;
import com.example.Backend.dto.Response.CommentResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("{blog-id}/add")
    public ResponseDto<CommentResponse> addComment(@PathVariable("blog-id") Long blogId, @RequestBody CommentRequest commentRequest, Authentication authentication) {
        return ResponseDto.success(commentService.addComment(blogId, commentRequest, authentication.getName()));
    }

    @PostMapping("{comment-id}/edit")
    public ResponseDto<CommentResponse> editComment(@PathVariable("comment-id") Long commentId, @RequestBody CommentRequest commentRequest, Authentication authentication) {
        return ResponseDto.success(commentService.editComment(commentId, commentRequest, authentication.getName()));
    }

    @PostMapping("{comment-id}/delete")
    public ResponseDto<String> deleteComment(@PathVariable("comment-id") Long commentId, Authentication authentication) {
        return ResponseDto.success(commentService.deleteComment(commentId, authentication.getName()));
    }
}
