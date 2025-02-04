package com.example.Backend.service;

public interface VerifyCodeService {
    void saveCode(String email, String code);
    boolean verifyCode(String email, String code);
}
