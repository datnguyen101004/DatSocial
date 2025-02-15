package com.example.Backend.mapper;

import com.example.Backend.dto.Response.SearchUserResponse;
import com.example.Backend.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T00:06:10+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (JetBrains s.r.o.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public SearchUserResponse toSearchUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        SearchUserResponse.SearchUserResponseBuilder searchUserResponse = SearchUserResponse.builder();

        searchUserResponse.id( user.getId() );

        searchUserResponse.fullName( user.getFullName() );

        return searchUserResponse.build();
    }
}
