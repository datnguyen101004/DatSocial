package com.example.Backend.mapper;

import com.example.Backend.dto.Request.RegisterDto;
import com.example.Backend.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-29T14:43:51+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.5 (JetBrains s.r.o.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(RegisterDto registerDto) {
        if ( registerDto == null ) {
            return null;
        }

        User user = new User();

        return user;
    }
}
