package com.example.Backend.service;

import com.example.Backend.dto.Request.LoginDto;
import com.example.Backend.dto.Request.RefreshTokenDto;
import com.example.Backend.dto.Request.RegisterDto;
import com.example.Backend.dto.Response.TokenResponseDto;

public interface AuthService {
    String register(RegisterDto registerDto);

    TokenResponseDto login(LoginDto loginDto);

    TokenResponseDto refresh(RefreshTokenDto refreshToken);

    TokenResponseDto verify(String code);

    TokenResponseDto loginWithGoogle(String email);

    String logout(String accessToken);
}
