package com.example.Backend.controller;

import com.example.Backend.dto.Response.FriendInfo;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.dto.Response.ListFriendResponse;
import com.example.Backend.dto.Response.UserResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("{user-id}/profile")
    public ResponseDto<UserResponse> profile(@PathVariable("user-id") Long userId) {
        return ResponseDto.success(userService.getProfile(userId));
    }

    @GetMapping("/{userId}/profile/share")
    public ResponseDto<UserResponse> profileShare(@PathVariable("userId") Long userId) {
        return ResponseDto.success(userService.profileShare(userId));
    }

    @GetMapping("/{userId}/profile/like")
    public ResponseDto<UserResponse> profileLike(@PathVariable("userId") Long userId) {
        return ResponseDto.success(userService.profileLike(userId));
    }

    @GetMapping("/friend/request")
    public ResponseDto<List<FriendResponse>> getAllFriendRequest(Authentication authentication) {
        return ResponseDto.success(userService.getAllFriendRequest(authentication.getName()));
    }

    @GetMapping("/{userId}/friend/all")
    public ResponseDto<List<FriendInfo>> getAllFriend(@PathVariable("userId") Long userId) {
        return ResponseDto.success(userService.getAllFriend(userId));
    }

    @GetMapping("/friend/all")
    public ResponseDto<ListFriendResponse> getAllMyFriend(Authentication authentication) {
        return ResponseDto.success(userService.getAllMyFriend(authentication.getName()));
    }
}
