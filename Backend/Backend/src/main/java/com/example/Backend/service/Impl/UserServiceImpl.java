package com.example.Backend.service.Impl;

import com.example.Backend.dto.Response.FriendInfo;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.dto.Response.ListFriendResponse;
import com.example.Backend.dto.Response.UserResponse;
import com.example.Backend.entity.Enum.FriendStatus;
import com.example.Backend.entity.Friend;
import com.example.Backend.entity.Like;
import com.example.Backend.entity.Share;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.mapper.BlogMapper;
import com.example.Backend.mapper.FriendMapper;
import com.example.Backend.repository.FriendRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;
    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;

    @Override
    public UserResponse profile(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .myBlog(user.getBlogs().stream().map(blogMapper::toBlogResponseDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserResponse profileShare(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));
        List<Share> shares = user.getShares();
        return UserResponse.builder()
                .fullName(user.getFullName())
                .myBlog(shares.stream().map(share -> blogMapper.toBlogResponseDto(share.getBlog())).collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserResponse profileLike(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));
        List<Like> likes = user.getLikes().stream()
                .filter(Like::isLiked)
                .toList();
        return UserResponse.builder()
                .fullName(user.getFullName())
                .myBlog(likes.stream().map(like -> blogMapper.toBlogResponseDto(like.getBlog())).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<FriendResponse> getAllFriendRequest(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        List<Friend> friends = friendRepository.findByFriendAndStatus(user, FriendStatus.valueOf("PENDING"));
        return friends.stream().map(friendMapper::toFriendResponse).collect(Collectors.toList());
    }

    @Override
    public List<FriendInfo> getAllFriend(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));
        List<Friend> friends = friendRepository.findByUserAndStatus(user, FriendStatus.valueOf("ACCEPTED"));
        List<Friend> friends1 = friendRepository.findByFriendAndStatus(user, FriendStatus.valueOf("ACCEPTED"));
        List<FriendInfo> friendListResponses1 = new java.util.ArrayList<>(friends.stream().map(friendMapper::toFriendListResponse).toList());
        List<FriendInfo> friendListResponses2 = friends1.stream().map(friendMapper::toUserListResponse).toList();
        friendListResponses1.addAll(friendListResponses2);
        return friendListResponses1;
    }

    @Override
    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .myBlog(user.getBlogs().stream().map(blogMapper::toBlogResponseDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public ListFriendResponse getAllMyFriend(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        List<Friend> friends = friendRepository.findByUserAndStatus(user, FriendStatus.valueOf("ACCEPTED"));
        List<Friend> friends1 = friendRepository.findByFriendAndStatus(user, FriendStatus.valueOf("ACCEPTED"));
        List<FriendInfo> friendListResponses1 = new java.util.ArrayList<>(friends.stream().map(friendMapper::toFriendListResponse).toList());
        List<FriendInfo> friendListResponses2 = friends1.stream().map(friendMapper::toUserListResponse).toList();
        friendListResponses1.addAll(friendListResponses2);
        return ListFriendResponse.builder()
                .friendList(friendListResponses1)
                .userId(user.getId())
                .build();
    }
}
