package com.example.Backend.repository;

import com.example.Backend.entity.Blog;
import com.example.Backend.entity.Like;
import com.example.Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByBlogAndUser(Blog blog, User user);

    Optional<Like> findByBlogAndUser(Blog blog, User user);
}
