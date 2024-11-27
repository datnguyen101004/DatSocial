package com.example.Backend.service.Impl;

import com.example.Backend.repository.BlogRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
}
