package com.example.Backend.mapper;

import com.example.Backend.dto.Response.SearchUserResponse;
import com.example.Backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    SearchUserResponse toSearchUserResponse(User user);
}
