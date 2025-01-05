package com.example.Backend.mapper;

import com.example.Backend.dto.Response.MessageResponse;
import com.example.Backend.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "sender", expression = "java(message.getSender() != null ? message.getSender().getFullName() : null)")
    @Mapping(target = "senderId", expression = "java(message.getSender().getId())")
    MessageResponse toMessageResponse(Message message);
}
