package com.example.Backend.service.Impl;

import com.example.Backend.dto.Request.CommentRequest;
import com.example.Backend.dto.Response.CommentResponse;
import com.example.Backend.entity.Blog;
import com.example.Backend.entity.Comment;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.exception.CustomException.NotPermissionException;
import com.example.Backend.mapper.CommentMapper;
import com.example.Backend.repository.BlogRepository;
import com.example.Backend.repository.CommentRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse addComment(Long blogId, CommentRequest commentRequest, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Blog not found"));
        Comment comment = Comment.builder()
                .content(commentRequest.getMessage())
                .user(user)
                .blog(blog)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    @Override
    public CommentResponse editComment(Long commentId, CommentRequest commentRequest, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getId().equals(user.getId())) {
            comment.setContent(commentRequest.getMessage());
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);
            return commentMapper.toCommentResponse(comment);
        }
        else {
            throw new NotPermissionException("You don't have permission to edit this comment");
        }
    }

    @Override
    public String deleteComment(Long commentId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).get();
        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.deleteById(commentId);
            return "Comment deleted successfully";
        }
        else {
            throw new NotPermissionException("You don't have permission to delete this comment");
        }
    }
}
