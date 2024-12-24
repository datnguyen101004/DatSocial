package com.example.Backend.mapper;

import com.example.Backend.dto.Response.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "sender", expression = "java(message.getSender().getFullName())")
    @Mapping(target = "roomId", expression = "java(message.getRoom().getRoomId())")
    MessageResponse toMessageResponse(com.example.Backend.entity.Message message);
}
