package com.example.Backend.service;

import com.example.Backend.dto.Response.FriendListResponse;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.dto.Response.SearchUserResponse;
import com.example.Backend.dto.Response.UserResponse;


import java.util.List;

public interface UserService {
    UserResponse profile(String name);

    UserResponse profileShare(String name);

    UserResponse profileLike(String name);

    List<FriendResponse> getAllFriendRequest(String name);

    List<FriendListResponse> getAllFriend(String name);

    List<SearchUserResponse> searchUser(String name, String email);
}
