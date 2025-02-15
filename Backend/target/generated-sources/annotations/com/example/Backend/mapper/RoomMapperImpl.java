package com.example.Backend.mapper;

import com.example.Backend.dto.Response.RoomResponse;
import com.example.Backend.entity.Room;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T00:06:10+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (JetBrains s.r.o.)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public RoomResponse toRoomResponse(Room room) {
        if ( room == null ) {
            return null;
        }

        RoomResponse.RoomResponseBuilder roomResponse = RoomResponse.builder();

        roomResponse.roomId( room.getRoomId() );

        roomResponse.userIds( mapUserIds(room) );
        roomResponse.messages( toMessageResponse(room.getMessages()) );

        return roomResponse.build();
    }
}
