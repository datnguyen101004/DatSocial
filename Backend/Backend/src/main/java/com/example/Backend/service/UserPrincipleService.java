package com.example.Backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserPrincipleService extends UserDetailsService {

    public UserDetails loadUserByUsername(String username);
}
