package com.example.Backend.mapper;

import com.example.Backend.dto.Response.FriendInfo;
import com.example.Backend.dto.Response.FriendResponse;
import com.example.Backend.entity.Friend;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T00:06:10+0700",
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
    public FriendInfo toFriendListResponse(Friend friend) {
        if ( friend == null ) {
            return null;
        }

        FriendInfo.FriendInfoBuilder friendInfo = FriendInfo.builder();

        friendInfo.id( friend.getFriend().getId() );
        friendInfo.fullName( friend.getFriend().getFullName() );

        return friendInfo.build();
    }

    @Override
    public FriendInfo toUserListResponse(Friend friend) {
        if ( friend == null ) {
            return null;
        }

        FriendInfo.FriendInfoBuilder friendInfo = FriendInfo.builder();

        friendInfo.id( friend.getUser().getId() );
        friendInfo.fullName( friend.getUser().getFullName() );

        return friendInfo.build();
    }
}
