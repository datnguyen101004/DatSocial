package com.example.Backend.mapper;

import com.example.Backend.dto.Response.MessageResponse;
import com.example.Backend.dto.Response.RoomResponse;
import com.example.Backend.entity.Message;
import com.example.Backend.entity.Room;
import com.example.Backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(target = "userIds" , expression = "java(mapUserIds(room))")
    @Mapping(target = "messages", expression = "java(toMessageResponse(room.getMessages()))")
    RoomResponse toRoomResponse(Room room);

    default List<Long> mapUserIds(Room room) {
        return room.getUsers().stream().map(User::getId).toList();
    }

    default List<MessageResponse> toMessageResponse(List<Message> messages) {
        return messages.stream()
                .map(message -> MessageResponse.builder()
                        .id(message.getId())
                        .sender(message.getSender() != null ? message.getSender().getFullName() : null)
                        .content(message.getContent())
                        .sendAt(message.getSendAt())
                        .build())
                .toList();
    }
}
