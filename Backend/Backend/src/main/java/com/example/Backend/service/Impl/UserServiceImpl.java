package com.example.Backend.service.Impl;

import com.example.Backend.dto.Response.UserResponse;
import com.example.Backend.entity.Like;
import com.example.Backend.entity.Share;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.mapper.BlogMapper;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;

    @Override
    public UserResponse profile(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        return UserResponse.builder()
                .fullName(user.getFullName())
                .myBlog(user.getBlogs().stream().map(blogMapper::toBlogResponseDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserResponse profileShare(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        List<Share> shares = user.getShares();
        return UserResponse.builder()
                .fullName(user.getFullName())
                .myBlog(shares.stream().map(share -> blogMapper.toBlogResponseDto(share.getBlog())).collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserResponse profileLike(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        List<Like> likes = user.getLikes().stream()
                .filter(Like::isLiked)
                .toList();
        return UserResponse.builder()
                .fullName(user.getFullName())
                .myBlog(likes.stream().map(like -> blogMapper.toBlogResponseDto(like.getBlog())).collect(Collectors.toList()))
                .build();
    }
}
