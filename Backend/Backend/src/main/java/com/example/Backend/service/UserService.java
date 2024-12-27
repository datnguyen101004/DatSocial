package com.example.Backend.service;

import com.example.Backend.dto.Response.FriendListResponse;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.dto.Response.SearchUserResponse;
import com.example.Backend.dto.Response.UserResponse;


import java.util.List;

public interface UserService {
    UserResponse profile(String name);

    UserResponse profileShare(Long id);

    UserResponse profileLike(Long id);

    List<FriendResponse> getAllFriendRequest(String name);

    List<FriendListResponse> getAllFriend(Long id);

    UserResponse getProfile(Long userId);

    List<FriendListResponse> getAllMyFriend(String name);
}
