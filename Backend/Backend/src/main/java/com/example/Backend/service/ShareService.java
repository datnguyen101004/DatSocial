package com.example.Backend.service;

import com.example.Backend.dto.Request.ShareRequest;
import com.example.Backend.dto.Response.ShareResponse;

public interface ShareService {
    String shareBlog(Long blogId, ShareRequest shareRequest, String email);
}
