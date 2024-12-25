package com.example.Backend.controller;

import com.example.Backend.dto.Request.SendMessage;
import com.example.Backend.dto.Response.MessageResponse;
import com.example.Backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/{roomId}")
    public MessageResponse sendMessage(@DestinationVariable String roomId,SendMessage sendMessage) {
        return messageService.sendMessage(roomId, sendMessage);
    }
}
