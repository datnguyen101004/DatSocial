package com.example.Backend.mapper;

import com.example.Backend.dto.Response.MessageResponse;
import com.example.Backend.entity.Message;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-25T16:01:16+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (JetBrains s.r.o.)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageResponse toMessageResponse(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageResponse.MessageResponseBuilder messageResponse = MessageResponse.builder();

        messageResponse.id( message.getId() );
        messageResponse.content( message.getContent() );
        messageResponse.sendAt( message.getSendAt() );

        messageResponse.sender( message.getSender() != null ? message.getSender().getFullName() : null );

        return messageResponse.build();
    }
}
