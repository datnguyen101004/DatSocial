package com.example.Backend.mapper;

import com.example.Backend.dto.Response.CommentResponse;
import com.example.Backend.entity.Comment;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-10T14:33:07+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (JetBrains s.r.o.)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponse toCommentResponse(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponse.CommentResponseBuilder commentResponse = CommentResponse.builder();

        commentResponse.commentId( comment.getId() );
        commentResponse.content( comment.getContent() );
        if ( comment.getCreatedAt() != null ) {
            commentResponse.createdAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( comment.getCreatedAt() ) );
        }
        if ( comment.getUpdatedAt() != null ) {
            commentResponse.updatedAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( comment.getUpdatedAt() ) );
        }

        commentResponse.userId( comment.getUser().getId() );
        commentResponse.fullName( comment.getUser().getFullName() );
        commentResponse.blogId( comment.getBlog().getId() );

        return commentResponse.build();
    }
}
