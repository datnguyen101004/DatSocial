package com.example.Backend.controller;

import com.example.Backend.dto.Request.CreateRoom;
import com.example.Backend.dto.Response.RoomResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.entity.Room;
import com.example.Backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    //Get messages in room
    @PostMapping("/messages")
    public ResponseDto<RoomResponse> getMessages(@RequestBody CreateRoom createRoom, Authentication authentication) {
        return ResponseDto.success(roomService.getMessages(createRoom, authentication.getName()));
    }
}
