package com.example.Backend.service.Impl;

import com.example.Backend.dto.Response.FriendListResponse;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.dto.Response.SearchUserResponse;
import com.example.Backend.dto.Response.UserResponse;
import com.example.Backend.entity.Enum.FriendStatus;
import com.example.Backend.entity.Friend;
import com.example.Backend.entity.Like;
import com.example.Backend.entity.Share;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.mapper.BlogMapper;
import com.example.Backend.mapper.FriendMapper;
import com.example.Backend.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    public UserResponse profile(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        return UserResponse.builder()
                .fullName(user.getFullName())
                .myBlog(user.getBlogs().stream().map(blogMapper::toBlogResponseDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserResponse profileShare(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        List<Share> shares = user.getShares();
        return UserResponse.builder()
                .fullName(user.getFullName())
                .myBlog(shares.stream().map(share -> blogMapper.toBlogResponseDto(share.getBlog())).collect(Collectors.toList()))
                .build();
    }

    @Override
    public UserResponse profileLike(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
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
    public List<FriendListResponse> getAllFriend(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(()->new NotFoundException("User not found"));
        List<Friend> friends = friendRepository.findByUserAndStatus(user, FriendStatus.valueOf("ACCEPTED"));
        List<Friend> friends1 = friendRepository.findByFriendAndStatus(user, FriendStatus.valueOf("ACCEPTED"));
        friends.addAll(friends1);
        return friends.stream().map(friendMapper::toFriendListResponse).collect(Collectors.toList());
    }

    @Override
    public List<SearchUserResponse> searchUser(String name, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("User not found"));
        List<User> users = userRepository.findByFullNameContainingIgnoreCase(name);
        return users.stream().map(userMapper::toSearchUserResponse).collect(Collectors.toList());
    }
}
