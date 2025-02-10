package com.example.Backend.controller;

import com.example.Backend.dto.Request.LoginDto;
import com.example.Backend.dto.Request.RefreshTokenDto;
import com.example.Backend.dto.Request.RegisterDto;
import com.example.Backend.dto.Response.TokenResponseDto;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseDto<String> register(@RequestBody RegisterDto registerDto) {
        return ResponseDto.success(authService.register(registerDto));
    }

    @GetMapping("/verify")
    public ResponseDto<TokenResponseDto> verify(@RequestParam String code) {
        return ResponseDto.success(authService.verify(code));
    }

    @PostMapping("/login")
    public ResponseDto<TokenResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseDto.success(authService.login(loginDto));
    }

    @PostMapping("/refresh")
    public ResponseDto<TokenResponseDto> refresh(@RequestBody RefreshTokenDto refreshToken) {
        return ResponseDto.success(authService.refresh(refreshToken));
    }
}
