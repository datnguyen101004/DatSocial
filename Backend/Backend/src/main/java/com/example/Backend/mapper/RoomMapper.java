package com.example.Backend.mapper;

import com.example.Backend.dto.Response.RoomResponse;
import com.example.Backend.entity.Room;
import com.example.Backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    @Mapping(target = "userIds" , expression = "java(mapUserIds(room))")
    RoomResponse toRoomResponse(Room room);

    default List<Long> mapUserIds(Room room) {
        return room.getUsers().stream().map(User::getId).toList();
    }
}
