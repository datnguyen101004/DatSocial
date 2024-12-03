package com.example.Backend.service.Impl;

import com.example.Backend.dto.Response.LikeResponse;
import com.example.Backend.entity.Blog;
import com.example.Backend.entity.Like;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.AlreadyException;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.repository.BlogRepository;
import com.example.Backend.repository.LikeRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public LikeResponse likeBlog(String type, Long id, String email) {
        //type : blog, avatar, comment, ...
        if (type.equalsIgnoreCase("blog")) {
            Blog blog = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
            if (likeRepository.existsByBlogAndUser(blog, user)) {
                throw new AlreadyException("Already liked");
            }
            Like like = Like.builder()
                    .blog(blog)
                    .user(user)
                    .build();
            likeRepository.save(like);
            return LikeResponse.builder()
                    .type("blog")
                    .id(blog.getId())
                    .fullName(user.getFullName())
                    .liked(true)
                    .build();
        }
        throw new NotFoundException("Type not found");
    }

    @Override
    public LikeResponse unlikeBlog(String type, Long id, String email) {
        if (type.equalsIgnoreCase("blog")) {
            Blog blog = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
            Like like = likeRepository.findByBlogAndUser(blog, user).orElseThrow(() -> new NotFoundException("You not liked"));
            likeRepository.delete(like);
            return LikeResponse.builder()
                    .type("blog")
                    .id(blog.getId())
                    .fullName(user.getFullName())
                    .liked(false)
                    .build();
        }
        throw new NotFoundException("Type not found");
    }

    @Override
    public Boolean isLiked(String type, Long id, String email) {
        if (type.equalsIgnoreCase("blog")) {
            Blog blog = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
            return likeRepository.existsByBlogAndUser(blog, user);
        }
        throw new NotFoundException("Type not found");
    }
}
