package com.example.Backend.repository;

import com.example.Backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM Blog b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', ?1, '%')) OR LOWER(b.content) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<Blog> searchBlogs(String search);
}
