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

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public String like(String type, Long id, String email) {
        //type : blog, avatar, comment, ...
        if (type.equalsIgnoreCase("blog")) {
            Blog blog = blogRepository.findById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
            Optional<Like> like = likeRepository.findByBlogAndUser(blog, user);
            if (like.isPresent()) {
                if (like.get().isLiked()){
                    like.get().setLiked(false);
                    likeRepository.save(like.get());
                    return "Unlike";
                }
                else {
                    like.get().setLiked(true);
                    likeRepository.save(like.get());
                    return "Like";
                }
            }
            else {
                Like like1 = Like.builder()
                        .blog(blog)
                        .user(user)
                        .liked(true)
                        .build();
                likeRepository.save(like1);
                return "Like";
            }
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
