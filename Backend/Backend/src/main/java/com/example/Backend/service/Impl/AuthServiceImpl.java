package com.example.Backend.service.Impl;

import com.example.Backend.Utils.HandleEmail;
import com.example.Backend.dto.Request.LoginDto;
import com.example.Backend.dto.Request.RefreshTokenDto;
import com.example.Backend.dto.Request.RegisterDto;
import com.example.Backend.dto.Response.TokenResponseDto;
import com.example.Backend.entity.Enum.LoginMethod;
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

import java.util.Optional;

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
        String email = registerDto.getEmail();
        String password = registerDto.getPassword();

        Optional<User> userOptional = userRepository.findByEmail(email);

        //Check if user already exists
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            //Check if login method is not local
            if (!user.getLoginMethod().equals(LoginMethod.LOCAL)) {
                String passwordEncoded = passwordEncoder.encode(password);
                user.setPassword(passwordEncoded);
                user.setLoginMethod(LoginMethod.LOCAL);
                userRepository.save(user);
            }
            else {
                if (!user.isEnable()) {  // Nếu user chưa xác thực, gửi lại email xác nhận
                    sendVerifyEmail(email);
                    return "User already exists but not verified. Please check your email.";
                }
                return "User already exists";
            }
        }
        else {
            //Create new user
            User newUser = User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .loginMethod(LoginMethod.LOCAL)
                    .roles(Roles.ROLE_USER)
                    .build();
            userRepository.save(newUser);
        }
        sendVerifyEmail(email);
        return "User registered successfully. Please check your email to verify your account";
    }

    private void sendVerifyEmail(String email) {
        String code = HandleEmail.createCode();
        verifyCodeService.saveCode(email, code);
        asyncService.sendEmail(email, "Welcome to our application", "You have successfully registered to our application. Please verify your account using this code: " + code);
    }

    @Override
    public TokenResponseDto login(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
            User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
            if (!user.isEnable()) {
                throw new InvalidCredentialException("Please verify your account");
            }
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
        if (email == null) {
            throw new InvalidCredentialException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.isEnable()) {
            throw new InvalidCredentialException("Please verify your account");
        }

        if (jwtService.isTokenExpired(refreshToken.getRefreshToken())) {
            throw new InvalidCredentialException("Refresh token expired");
        }

        String token = jwtService.generateAccessToken(email);
        return new TokenResponseDto(token, refreshToken.getRefreshToken());
    }

    @Override
    public TokenResponseDto verify(String code) {
        String email = verifyCodeService.verifyCode(code);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        user.setEnable(true);
        userRepository.save(user);
        return new TokenResponseDto(jwtService.generateAccessToken(email), jwtService.generateRefreshToken(email));
    }

    @Override
    public TokenResponseDto loginWithGoogle(String email) {
        // Work flow: Check if the user is already in the database. If not, create a new user
        // with the email and login_method is "google". Then generate a new access token for the user.
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            User newUser = User.builder()
                    .fullName("dat")
                    .password("dat")
                    .email(email)
                    .loginMethod(LoginMethod.GOOGLE)
                    .roles(Roles.ROLE_USER)
                    .enable(true)
                    .build();
            userRepository.save(newUser);
        }
        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);
        return new TokenResponseDto(accessToken, refreshToken);
    }
}
