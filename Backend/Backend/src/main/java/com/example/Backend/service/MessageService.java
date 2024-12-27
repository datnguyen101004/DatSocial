package com.example.Backend.service;

import com.example.Backend.dto.Request.SendMessage;
import com.example.Backend.dto.Response.MessageResponse;

public interface MessageService {
    MessageResponse sendMessage(String roomId, SendMessage sendMessage, String email);
}
