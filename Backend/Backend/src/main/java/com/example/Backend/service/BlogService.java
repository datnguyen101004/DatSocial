package com.example.Backend.service;

import com.example.Backend.dto.Request.CreateBlogDto;
import com.example.Backend.dto.Response.BlogResponseDto;

import java.util.List;

public interface BlogService {
    BlogResponseDto createBlog(CreateBlogDto createBlogDto, String email);

    BlogResponseDto getBlogById(Long id);

    List<BlogResponseDto> getAllBlogs();

    String deleteBlog(Long id, String name);

    BlogResponseDto editBlog(Long id, CreateBlogDto createBlogDto, String name);
}
