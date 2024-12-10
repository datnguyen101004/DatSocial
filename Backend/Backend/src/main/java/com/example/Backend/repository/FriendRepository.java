package com.example.Backend.repository;

import com.example.Backend.entity.Enum.FriendStatus;
import com.example.Backend.entity.Friend;
import com.example.Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByUserAndFriend(User user, User friend);

    List<Friend> findByFriendAndStatus(User friend, FriendStatus status);

    List<Friend> findByUserAndStatus(User user, FriendStatus status);

    Optional<Friend> findByUserAndFriendAndStatus(User friend, User user, FriendStatus pending);
}
