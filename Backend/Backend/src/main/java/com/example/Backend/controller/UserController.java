package com.example.Backend.controller;

import com.example.Backend.dto.Response.UserResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseDto<UserResponse> profile(Authentication authentication) {
        return ResponseDto.success(userService.profile(authentication.getName()));
    }

    @GetMapping("/profile/share")
    public ResponseDto<UserResponse> profileShare(Authentication authentication) {
        return ResponseDto.success(userService.profileShare(authentication.getName()));
    }

    @GetMapping("/profile/like")
    public ResponseDto<UserResponse> profileLike(Authentication authentication) {
        return ResponseDto.success(userService.profileLike(authentication.getName()));
    }

    @GetMapping("/profile/friend/request")
    public ResponseDto<UserResponse> profileFriendRequest(Authentication authentication) {
        return ResponseDto.success(userService.profileFriendRequest(authentication.getName()));
    }
}
