package com.example.Backend.service;

public interface EmailService {
    void sendEmail(String email, String subject, String text);
}
