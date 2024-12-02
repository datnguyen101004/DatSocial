package com.example.Backend.controller;

import com.example.Backend.dto.Response.LikeResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{content-type}/{content-id}/like")
    public ResponseDto<LikeResponse> likeBlog(@PathVariable("content-type") String type, @PathVariable("content-id") Long id, Authentication authentication) {
        return ResponseDto.success(likeService.likeBlog(type, id, authentication.getName()));
    }

    @PostMapping("/{content-type}/{content-id}/unlike")
    public ResponseDto<LikeResponse> unlikeBlog(@PathVariable("content-type") String type, @PathVariable("content-id") Long id, Authentication authentication) {
        return ResponseDto.success(likeService.unlikeBlog(type, id, authentication.getName()));
    }

    @GetMapping("/{content-type}/{content-id}/isLiked")
    public ResponseDto<Boolean> isLiked(@PathVariable("content-type") String type, @PathVariable("content-id") Long id, Authentication authentication) {
        return ResponseDto.success(likeService.isLiked(type, id , authentication.getName()));
    }
}
