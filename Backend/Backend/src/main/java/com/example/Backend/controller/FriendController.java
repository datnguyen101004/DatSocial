package com.example.Backend.controller;

import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1/friend")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/{friend-id}/sendRequest")
    public ResponseDto<FriendResponse> sendRequest(@PathVariable("friend-id") Long friendId, Authentication authentication) {
        return ResponseDto.success(friendService.sendRequest(friendId, authentication.getName()));
    }

    @PostMapping("/{friend-id}/acceptRequest")
    public ResponseDto<FriendResponse> acceptRequest(@PathVariable("friend-id") Long friendId, Authentication authentication) {
        return ResponseDto.success(friendService.acceptRequest(friendId, authentication.getName()));
    }

    @PostMapping("/{friend-id}/rejectRequest")
    public ResponseDto<FriendResponse> rejectRequest(@PathVariable("friend-id") Long friendId, Authentication authentication) {
        return ResponseDto.success(friendService.rejectRequest(friendId, authentication.getName()));
    }

    @PostMapping("/{friend-id}/unFriend")
    public ResponseDto<FriendResponse> unFriend(@PathVariable("friend-id") Long friendId, Authentication authentication) {
        return ResponseDto.success(friendService.unFriend(friendId, authentication.getName()));
    }

    @PostMapping("/{friend-id}/block")
    public ResponseDto<FriendResponse> blockFriend(@PathVariable("friend-id") Long friendId, Authentication authentication) {
        return ResponseDto.success(friendService.blockFriend(friendId, authentication.getName()));
    }
}
