package com.example.Backend.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    Date extractExpiredTime(String token);
}
