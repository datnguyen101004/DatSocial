package com.example.Backend.repository;

import com.example.Backend.entity.Blog;
import com.example.Backend.entity.Share;
import com.example.Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Optional<Share> findByBlogAndUser(Blog blog, User user);
}
