package com.example.Backend.service.Impl;
import com.example.Backend.Utils.HandleChat;
import com.example.Backend.dto.Request.CreateRoom;
import com.example.Backend.dto.Response.RoomResponse;
import com.example.Backend.entity.Room;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.AlreadyException;
import com.example.Backend.exception.CustomException.NotPermissionException;
import com.example.Backend.mapper.RoomMapper;
import com.example.Backend.repository.RoomRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public RoomResponse createRoom(CreateRoom createRoom, String name) {
        User user1 = userRepository.findByEmail(name).orElseThrow(() -> new NotPermissionException("Not permission"));
        User user2 = userRepository.findById(createRoom.getReceiverId()).orElseThrow(() -> new NotPermissionException("Not permission"));
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        Room room1 = roomRepository.findByRoomId(HandleChat.generateRoomId(user1.getId(), user2.getId()));
        if (room1 != null) {
            throw new AlreadyException("Room already exists");
        }
        Room room = new Room();
        String roomId = HandleChat.generateRoomId(user1.getId(), user2.getId());
        room.setRoomId(roomId);
        room.setUsers(users);
        room.setMessages(new ArrayList<>());
        roomRepository.save(room);
        return roomMapper.toRoomResponse(room);
    }
}
