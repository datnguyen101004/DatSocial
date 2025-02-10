package com.example.Backend.service;

public interface VerifyCodeService {
    void saveCode(String email, String code);
    String verifyCode(String code);
}
