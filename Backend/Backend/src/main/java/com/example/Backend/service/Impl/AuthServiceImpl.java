package com.example.Backend.service.Impl;

import com.example.Backend.dto.Request.LoginDto;
import com.example.Backend.dto.Request.RegisterDto;
import com.example.Backend.dto.Response.TokenResponseDto;
import com.example.Backend.entity.Enum.Roles;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.EmailNotExistException;
import com.example.Backend.exception.CustomException.InvalidCredentialException;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.AuthService;
import com.example.Backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponseDto register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new EmailNotExistException("Email is already in use");
        }
        User user = new User();
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Roles.ROLE_USER);
        user.setEnable(true);
        userRepository.save(user);
        String token = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        return new TokenResponseDto(token, refreshToken);
    }

    @Override
    public TokenResponseDto login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            User user = userRepository.findByEmail(loginDto.getEmail()).get();
            String token = jwtService.generateAccessToken(user.getEmail());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail());
            return new TokenResponseDto(token, refreshToken);
        } catch (Exception e) {
            throw new InvalidCredentialException("Invalid email or password");
        }
    }
}
