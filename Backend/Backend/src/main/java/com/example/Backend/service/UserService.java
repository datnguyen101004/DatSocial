package com.example.Backend.service;

import com.example.Backend.dto.Response.UserResponse;

public interface UserService {
    UserResponse profile(String name);

    UserResponse profileShare(String name);

    UserResponse profileLike(String name);
}
