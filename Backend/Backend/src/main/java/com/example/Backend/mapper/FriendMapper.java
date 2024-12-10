package com.example.Backend.mapper;

import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.entity.Friend;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    @Mapping(target = "userId", expression = "java(friend.getUser().getId())")
    @Mapping(target = "friendId", expression = "java(friend.getFriend().getId())")
    FriendResponse toFriendResponse(Friend friend);
}
