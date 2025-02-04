package com.example.Backend.service.Impl;

import com.example.Backend.service.AsyncService;
import com.example.Backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService {
    private final EmailService emailService;

    @Async
    public void sendEmail(String email, String subject, String text) {
        // send email
        emailService.sendEmail(email, subject, text);
    }
}
