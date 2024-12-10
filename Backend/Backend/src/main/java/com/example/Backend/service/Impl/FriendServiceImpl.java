package com.example.Backend.service.Impl;

import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.entity.Enum.FriendStatus;
import com.example.Backend.entity.Friend;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.mapper.FriendMapper;
import com.example.Backend.repository.FriendRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final FriendMapper friendMapper;

    @Override
    public FriendResponse sendRequest(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        if (friendRepository.findByUserAndFriend(user, friend).isPresent()) {
            throw new NotFoundException("You have sent request to this user");
        }
        Friend friend1 = Friend.builder()
                .user(user)
                .friend(friend)
                .status(FriendStatus.PENDING)
                .build();
        friendRepository.save(friend1);
        return friendMapper.toFriendResponse(friend1);
    }

    @Override
    public FriendResponse acceptRequest(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        if (friendRepository.findByUserAndFriend(friend, user).isEmpty()) {
            throw new NotFoundException("You have not received request from this user");
        }
        Friend friend1 = friendRepository.findByUserAndFriend(friend, user).get();
        friend1.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friend1);
        return friendMapper.toFriendResponse(friend1);
    }

    @Override
    public FriendResponse rejectRequest(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        if (friendRepository.findByUserAndFriend(friend, user).isEmpty()) {
            throw new NotFoundException("You have not received request from this user");
        }
        Friend friend1 = friendRepository.findByUserAndFriend(friend, user).get();
        friend1.setStatus(FriendStatus.REJECTED);
        friendRepository.save(friend1);
        return friendMapper.toFriendResponse(friend1);
    }

    @Override
    public FriendResponse unFriend(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        Optional<Friend> friend1 = friendRepository.findByUserAndFriend(user, friend);
        if (friend1.isEmpty()) {
            throw new NotFoundException("You have not been friend with this user");
        }
        friendRepository.delete(friend1.get());
        return friendMapper.toFriendResponse(friend1.get());
    }

    @Override
    public FriendResponse blockFriend(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        Optional<Friend> friend1 = friendRepository.findByUserAndFriend(user, friend);
        if (friend1.isEmpty()) {
            throw new NotFoundException("You have not been friend with this user");
        }
        friend1.get().setStatus(FriendStatus.BLOCKED);
        friendRepository.save(friend1.get());
        return friendMapper.toFriendResponse(friend1.get());
    }
}
