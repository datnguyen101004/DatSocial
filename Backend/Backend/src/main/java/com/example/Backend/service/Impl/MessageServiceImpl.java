package com.example.Backend.service.Impl;

import com.example.Backend.dto.Request.SendMessage;
import com.example.Backend.dto.Response.MessageResponse;
import com.example.Backend.entity.Message;
import com.example.Backend.entity.Room;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.mapper.MessageMapper;
import com.example.Backend.repository.RoomRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageResponse sendMessage(String roomId, SendMessage sendMessage) {
        Room room = roomRepository.findByRoomId(roomId).orElseThrow(()-> new NotFoundException("Room not exist"));
        User user = userRepository.findById(sendMessage.getSenderId()).orElseThrow(()-> new NotFoundException("User not exist"));
        Message message = Message.builder()
                .content(sendMessage.getContent())
                .sender(user)
                .sendAt(LocalDateTime.now())
                .room(room)
                .build();
        return messageMapper.toMessageResponse(message);
    }
}
