package com.example.Backend.service.Impl;

import com.example.Backend.dto.Request.CreateBlogDto;
import com.example.Backend.dto.Response.BlogResponseDto;
import com.example.Backend.entity.Blog;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.EmailNotExistException;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.exception.CustomException.UnauthorizedException;
import com.example.Backend.mapper.BlogMapper;
import com.example.Backend.repository.BlogRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.BlogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;

    @Override
    public BlogResponseDto createBlog(CreateBlogDto createBlogDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotExistException("User not found"));
        Blog blog = Blog.builder()
                .title(createBlogDto.getTitle())
                .content(createBlogDto.getContent())
                .author(user)
                .updatedAt(LocalDateTime.now())
                .build();
        blogRepository.save(blog);
        return blogMapper.toBlogResponseDto(blog);
    }

    @Override
    public BlogResponseDto getBlogById(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
        return blogMapper.toBlogResponseDto(blog);
    }

    @Override
    public List<BlogResponseDto> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        List<BlogResponseDto> blogResponseDtos = new ArrayList<>();
        for (Blog blog : blogs) {
            blogResponseDtos.add(blogMapper.toBlogResponseDto(blog));
        }
        return blogResponseDtos;
    }

    @Transactional
    @Override
    public String deleteBlog(Long id, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
        if (blog.getAuthor().getEmail().equals(user.getEmail())) {
            blogRepository.deleteById(id);
            return "Delete blog successfully";
        }
        else {
            throw new UnauthorizedException("You are not allowed to delete this blog");
        }
    }

    @Override
    public BlogResponseDto editBlog(Long id, CreateBlogDto createBlogDto, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
        if (blog.getAuthor().getEmail().equals(user.getEmail())) {
            blog.setTitle(createBlogDto.getTitle());
            blog.setContent(createBlogDto.getContent());
            blogRepository.save(blog);
            return blogMapper.toBlogResponseDto(blog);
        }
        else {
            throw new UnauthorizedException("You are not allowed to edit this blog");
        }
    }
}
