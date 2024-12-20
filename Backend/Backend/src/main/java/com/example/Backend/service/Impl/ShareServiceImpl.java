package com.example.Backend.service.Impl;

import com.example.Backend.dto.Request.ShareRequest;
import com.example.Backend.entity.Blog;
import com.example.Backend.entity.Enum.DisplayZone;
import com.example.Backend.entity.Share;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.AlreadyException;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.mapper.BlogMapper;
import com.example.Backend.repository.BlogRepository;
import com.example.Backend.repository.ShareRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShareServiceImpl implements ShareService {
    private final ShareRepository shareRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;

    @Override
    public String shareBlog(Long blogId, ShareRequest shareRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Blog not found"));
        if (shareRepository.findByBlogAndUser(blog, user).isPresent()) {
            throw new AlreadyException("You have shared this blog");
        }
        try {
            Share share = Share.builder()
                    .blog(blog)
                    .user(user)
                    .displayZone(DisplayZone.valueOf(shareRequest.getDisplayZone()))
                    .message(shareRequest.getMessage())
                    .build();
            shareRepository.save(share);
            return "Share blog successfully";
        }
        catch (Exception e) {
            throw new NotFoundException("Display zone not found");
        }
    }

    @Override
    public String deleteShare(Long blogId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Blog not found"));
        Share share = shareRepository.findByBlogAndUser(blog, user).orElseThrow(() -> new NotFoundException("Share not found"));
        shareRepository.delete(share);
        return "Delete share successfully";
    }

    @Override
    public Boolean isShared(Long blogId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Blog not found"));
        return shareRepository.findByBlogAndUser(blog, user).isPresent();
    }
}
