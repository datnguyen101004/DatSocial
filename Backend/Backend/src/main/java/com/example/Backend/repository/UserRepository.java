package com.example.Backend.repository;

import com.example.Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.ScopedValue;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
