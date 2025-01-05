package com.example.Backend.service.Impl;

import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.entity.Enum.FriendStatus;
import com.example.Backend.entity.Friend;
import com.example.Backend.entity.User;
import com.example.Backend.exception.CustomException.AlreadyException;
import com.example.Backend.exception.CustomException.NotFoundException;
import com.example.Backend.mapper.FriendMapper;
import com.example.Backend.repository.FriendRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Optional<Friend> friend2 = friendRepository.findByUserAndFriend(user, friend);
        Optional<Friend> friend3 = friendRepository.findByUserAndFriend(friend, user);
        if (friend2.isPresent()) {
            if (friend2.get().getStatus().equals(FriendStatus.valueOf("PENDING"))) {
                friend2.get().setStatus(FriendStatus.NONE);
                friendRepository.save(friend2.get());
                return friendMapper.toFriendResponse(friend2.get());
            }
            if (friend2.get().getStatus().equals(FriendStatus.valueOf("BLOCKED"))) {
                throw new AlreadyException("You are blocked by this user");
            }
            if (friend2.get().getStatus().equals(FriendStatus.valueOf("ACCEPTED"))) {
                throw new AlreadyException("You are already friend with this user");
            }
            if (friend2.get().getStatus().equals(FriendStatus.valueOf("NONE"))) {
                friend2.get().setStatus(FriendStatus.PENDING);
                friendRepository.save(friend2.get());
                return friendMapper.toFriendResponse(friend2.get());
            }
        }
        if (friend3.isPresent()) {
            if (friend3.get().getStatus().equals(FriendStatus.valueOf("PENDING"))) {
                friend3.get().setStatus(FriendStatus.NONE);
                friendRepository.save(friend3.get());
                return friendMapper.toFriendResponse(friend3.get());
            }
            if (friend3.get().getStatus().equals(FriendStatus.valueOf("BLOCKED"))) {
                throw new AlreadyException("You are blocked by this user");
            }
            if (friend3.get().getStatus().equals(FriendStatus.valueOf("ACCEPTED"))) {
                throw new AlreadyException("You are already friend with this user");
            }
            if (friend3.get().getStatus().equals(FriendStatus.valueOf("NONE"))) {
                friend3.get().setStatus(FriendStatus.PENDING);
                friendRepository.save(friend3.get());
                return friendMapper.toFriendResponse(friend3.get());
            }
        }
        Friend friend1 = Friend.builder()
                .user(user)
                .friend(friend)
                .status(FriendStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        friendRepository.save(friend1);
        return friendMapper.toFriendResponse(friend1);
    }

    @Override
    public FriendResponse acceptRequest(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        Optional<Friend> friend1 = friendRepository.findByUserAndFriendAndStatus(friend, user, FriendStatus.valueOf("PENDING"));
        if (friend1.isEmpty()) {
            throw new NotFoundException("You have not received request from this user");
        }
        friend1.get().setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friend1.get());
        return friendMapper.toFriendResponse(friend1.get());
    }

    @Override
    public FriendResponse rejectRequest(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        Optional<Friend> friend1 = friendRepository.findByUserAndFriendAndStatus(friend, user, FriendStatus.valueOf("PENDING"));
        if (friend1.isEmpty()) {
            throw new NotFoundException("You have not received request from this user");
        }
        friend1.get().setStatus(FriendStatus.NONE);
        friendRepository.save(friend1.get());
        return friendMapper.toFriendResponse(friend1.get());
    }

    @Override
    public FriendResponse unFriend(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        Optional<Friend> friend1 = friendRepository.findByUserAndFriendAndStatus(user, friend, FriendStatus.valueOf("ACCEPTED"));
        Optional<Friend> friend2 = friendRepository.findByUserAndFriendAndStatus(friend, user, FriendStatus.valueOf("ACCEPTED"));
        if (friend1.isEmpty() && friend2.isEmpty()) {
            throw new NotFoundException("You have not been friend with this user");
        }
        if (friend1.isPresent()) {
            friend1.get().setStatus(FriendStatus.NONE);
            friendRepository.save(friend1.get());
            return friendMapper.toFriendResponse(friend1.get());
        }
        friend2.get().setStatus(FriendStatus.NONE);
        friendRepository.save(friend2.get());
        return friendMapper.toFriendResponse(friend2.get());
    }

    @Override
    public FriendResponse blockFriend(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        Friend friend1 = Friend.builder()
                .user(user)
                .friend(friend)
                .status(FriendStatus.BLOCKED)
                .createdAt(LocalDateTime.now())
                .build();
        return friendMapper.toFriendResponse(friend1);
    }

    @Override
    public FriendResponse cancelRequest(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        Optional<Friend> friend1 = friendRepository.findByUserAndFriendAndStatus(user, friend, FriendStatus.valueOf("PENDING"));
        if (friend1.isEmpty()) {
            throw new NotFoundException("You have not sent request to this user");
        }
        friend1.get().setStatus(FriendStatus.NONE);
        friendRepository.save(friend1.get());
        return friendMapper.toFriendResponse(friend1.get());
    }

    @Override
    public String status(Long friendId, String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new NotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new NotFoundException("Friend not found"));
        if (user.getId().equals(friend.getId())) {
            return "ME";
        }
        Optional<Friend> friend1 = friendRepository.findByUserAndFriendAndStatus(user, friend, FriendStatus.valueOf("ACCEPTED"));
        Optional<Friend> friend2 = friendRepository.findByUserAndFriendAndStatus(friend, user, FriendStatus.valueOf("ACCEPTED"));
        if (friend1.isPresent() || friend2.isPresent()) {
            return "FRIEND";
        }
        Optional<Friend> friend3 = friendRepository.findByUserAndFriendAndStatus(user, friend, FriendStatus.valueOf("PENDING"));
        if (friend3.isPresent()) {
            return "PENDING";
        }
        Optional<Friend> friend4 = friendRepository.findByUserAndFriendAndStatus(friend, user, FriendStatus.valueOf("PENDING"));
        if (friend4.isPresent()) {
            return "RECEIVED";
        }
        return "NONE";
    }
}
