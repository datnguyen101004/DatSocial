package com.example.Backend.service;

import com.example.Backend.dto.Request.CreateRoom;
import com.example.Backend.dto.Response.RoomResponse;
import com.example.Backend.entity.Room;

public interface RoomService {

    RoomResponse getMessages(CreateRoom createRoom, String name);
}
