package com.example.Backend.controller;

import com.example.Backend.dto.Request.CreateBlogDto;
import com.example.Backend.dto.Response.BlogResponseDto;
import com.example.Backend.dto.Response.CommentResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1/blog")
public class BlogController {
    private final BlogService blogService;

    @PostMapping("/create")
    public ResponseDto<BlogResponseDto> createBlog(@RequestBody CreateBlogDto createBlogDto, Authentication authentication) {
        return ResponseDto.success(blogService.createBlog(createBlogDto, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseDto<BlogResponseDto> getBlogById(@PathVariable Long id) {
        return ResponseDto.success(blogService.getBlogById(id));
    }

    @GetMapping("/all")
    public ResponseDto<List<BlogResponseDto>> getAllBlogs() {
        return ResponseDto.success(blogService.getAllBlogs());
    }

    @PostMapping("/delete/{id}")
    public ResponseDto<String> deleteBlog(@PathVariable Long id, Authentication authentication) {
        return ResponseDto.success(blogService.deleteBlog(id, authentication.getName()));
    }

    @PostMapping("/edit/{id}")
    public ResponseDto<BlogResponseDto> editBlog(@PathVariable Long id, @RequestBody CreateBlogDto createBlogDto, Authentication authentication) {
        return ResponseDto.success(blogService.editBlog(id, createBlogDto, authentication.getName()));
    }

    @GetMapping("/{id}/comment")
    public ResponseDto<List<CommentResponse>> getAllComments(@PathVariable Long id) {
        return ResponseDto.success(blogService.getAllComments(id));
    }
}
