package com.example.Backend.service;

import com.example.Backend.dto.Response.LikeResponse;

public interface LikeService {
    String like(String type, Long id, String email);

    Boolean isLiked(String type, Long id, String email);

}
