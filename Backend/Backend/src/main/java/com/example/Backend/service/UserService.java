package com.example.Backend.service;

import com.example.Backend.dto.Response.FriendInfo;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.dto.Response.ListFriendResponse;
import com.example.Backend.dto.Response.UserResponse;


import java.util.List;

public interface UserService {
    UserResponse profile(String name);

    UserResponse profileShare(Long id);

    UserResponse profileLike(Long id);

    List<FriendResponse> getAllFriendRequest(String name);

    List<FriendInfo> getAllFriend(Long id);

    UserResponse getProfile(Long userId);

    ListFriendResponse getAllMyFriend(String name);
}
