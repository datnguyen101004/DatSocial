package com.example.Backend.controller;

import com.example.Backend.dto.Response.FriendListResponse;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.dto.Response.SearchUserResponse;
import com.example.Backend.dto.Response.UserResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/profile/share")
    public ResponseDto<UserResponse> profileShare(Authentication authentication) {
        return ResponseDto.success(userService.profileShare(authentication.getName()));
    }

    @GetMapping("/profile/like")
    public ResponseDto<UserResponse> profileLike(Authentication authentication) {
        return ResponseDto.success(userService.profileLike(authentication.getName()));
    }

    @GetMapping("/friend/request")
    public ResponseDto<List<FriendResponse>> getAllFriendRequest(Authentication authentication) {
        return ResponseDto.success(userService.getAllFriendRequest(authentication.getName()));
    }

    @GetMapping("/friend/all")
    public ResponseDto<List<FriendListResponse>> getAllFriend(Authentication authentication) {
        return ResponseDto.success(userService.getAllFriend(authentication.getName()));
    }

    @GetMapping("/search")
    public ResponseDto<List<SearchUserResponse>> searchUser(@RequestParam("name") String name, Authentication authentication) {
        return ResponseDto.success(userService.searchUser(name, authentication.getName()));
    }
}
