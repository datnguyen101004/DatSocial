package com.example.Backend.service.Impl;

import com.example.Backend.entity.User;
import com.example.Backend.entity.UserPrinciple;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.UserPrincipleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserPrincipleServiceImpl implements UserPrincipleService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User Not Found with email: " + email);
        }
        return new UserPrinciple(user);
    }
}
