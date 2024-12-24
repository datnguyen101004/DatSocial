package com.example.Backend.service;

import com.example.Backend.dto.Request.CreateRoom;
import com.example.Backend.dto.Response.RoomResponse;

public interface RoomService {
    RoomResponse createRoom(CreateRoom createRoom, String name);
}
