package com.example.Backend.service;

import com.example.Backend.dto.Request.ShareRequest;

public interface ShareService {
    String shareBlog(Long blogId, ShareRequest shareRequest, String email);

    String deleteShare(Long blogId, String name);
}
