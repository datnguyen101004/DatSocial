package com.example.Backend.controller;

import com.example.Backend.dto.Request.ShareRequest;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1/share")
public class ShareController {
    private final ShareService shareService;

    @PostMapping("/{blog-id}")
    public ResponseDto<String> shareBlog(@PathVariable("blog-id") Long blogId, @RequestBody ShareRequest shareRequest, Authentication authentication) {
        return ResponseDto.success(shareService.shareBlog(blogId, shareRequest, authentication.getName()));
    }

    @PostMapping("/{blog-id}/delete")
    public ResponseDto<String> deleteShare(@PathVariable("blog-id") Long blogId, Authentication authentication) {
        return ResponseDto.success(shareService.deleteShare(blogId, authentication.getName()));
    }
}
