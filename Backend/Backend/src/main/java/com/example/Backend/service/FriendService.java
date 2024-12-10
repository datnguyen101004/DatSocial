package com.example.Backend.service;

import com.example.Backend.dto.Response.FriendResponse;

public interface FriendService {
    FriendResponse sendRequest(Long friendId, String name);

    FriendResponse acceptRequest(Long friendId, String name);

    FriendResponse rejectRequest(Long friendId, String name);

    FriendResponse unFriend(Long friendId, String name);

    FriendResponse blockFriend(Long friendId, String name);
}
