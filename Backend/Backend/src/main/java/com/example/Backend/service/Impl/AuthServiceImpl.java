package com.example.Backend.service.Impl;

import com.example.Backend.Utils.HandleEmail;
import com.example.Backend.dto.Request.LoginDto;
import com.example.Backend.dto.Request.RefreshTokenDto;
import com.example.Backend.dto.Request.RegisterDto;
import com.example.Backend.dto.Response.TokenResponseDto;
import com.example.Backend.entity.Enum.Roles;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.exception.CustomException.InvalidCredentialException;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.AsyncService;
import com.example.Backend.service.AuthService;
import com.example.Backend.service.JwtService;
import com.example.Backend.service.VerifyCodeService;
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
    private final AsyncService asyncService;
    private final VerifyCodeService verifyCodeService;

    @Override
    public String register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new NotFoundException("Email is already in use");
        }
        User user = new User();
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Roles.ROLE_USER);
        userRepository.save(user);
        String code = HandleEmail.createCode();
        verifyCodeService.saveCode(user.getEmail(), code);
        asyncService.sendEmail(user.getEmail(), "Welcome to our application", "You have successfully registered to our application. Please verify your account using this code: " + code);
        return "User registered successfully. Please check your email to verify your account";
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

    @Override
    public TokenResponseDto refresh(RefreshTokenDto refreshToken) {
        String email = jwtService.extractUsername(refreshToken.getRefreshToken());
        if (email != null) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
            if (!jwtService.isTokenExpired(refreshToken.getRefreshToken())) {
                String token = jwtService.generateAccessToken(email);
                return new TokenResponseDto(token, refreshToken.getRefreshToken());
            }
            else {
                throw new InvalidCredentialException("Invalid refresh token");
            }
        }
        else {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public TokenResponseDto verify(String code) {
        String email = verifyCodeService.verifyCode(code);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        user.setEnable(true);
        userRepository.save(user);
        return new TokenResponseDto(jwtService.generateAccessToken(email), jwtService.generateRefreshToken(email));
    }
}
