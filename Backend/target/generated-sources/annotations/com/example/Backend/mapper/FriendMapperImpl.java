package com.example.Backend.mapper;

import com.example.Backend.dto.Response.FriendListResponse;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.entity.Friend;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-24T18:38:35+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (JetBrains s.r.o.)"
)
@Component
public class FriendMapperImpl implements FriendMapper {

    @Override
    public FriendResponse toFriendResponse(Friend friend) {
        if ( friend == null ) {
            return null;
        }

        FriendResponse.FriendResponseBuilder friendResponse = FriendResponse.builder();

        if ( friend.getStatus() != null ) {
            friendResponse.status( friend.getStatus().name() );
        }
        friendResponse.createdAt( friend.getCreatedAt() );

        friendResponse.userId( friend.getUser().getId() );
        friendResponse.friendId( friend.getFriend().getId() );

        return friendResponse.build();
    }

    @Override
    public FriendListResponse toFriendListResponse(Friend friend) {
        if ( friend == null ) {
            return null;
        }

        FriendListResponse.FriendListResponseBuilder friendListResponse = FriendListResponse.builder();

        friendListResponse.id( friend.getFriend().getId() );
        friendListResponse.fullName( friend.getFriend().getFullName() );

        return friendListResponse.build();
    }

    @Override
    public FriendListResponse toUserListResponse(Friend friend) {
        if ( friend == null ) {
            return null;
        }

        FriendListResponse.FriendListResponseBuilder friendListResponse = FriendListResponse.builder();

        friendListResponse.id( friend.getUser().getId() );
        friendListResponse.fullName( friend.getUser().getFullName() );

        return friendListResponse.build();
    }
}
