package com.example.Backend.mapper;

import com.example.Backend.dto.Response.BlogResponseDto;
import com.example.Backend.entity.Blog;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-17T23:53:15+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (JetBrains s.r.o.)"
)
@Component
public class BlogMapperImpl implements BlogMapper {

    @Override
    public BlogResponseDto toBlogResponseDto(Blog blog) {
        if ( blog == null ) {
            return null;
        }

        BlogResponseDto.BlogResponseDtoBuilder blogResponseDto = BlogResponseDto.builder();

        blogResponseDto.id( blog.getId() );
        blogResponseDto.title( blog.getTitle() );
        blogResponseDto.content( blog.getContent() );
        blogResponseDto.createdAt( blog.getCreatedAt() );
        blogResponseDto.updatedAt( blog.getUpdatedAt() );

        blogResponseDto.author( blog.getAuthor().getFullName() );
        blogResponseDto.likesCount( countLikes(blog) );
        blogResponseDto.commentsCount( blog.getComments().size() );
        blogResponseDto.sharesCount( blog.getShares().size() );

        return blogResponseDto.build();
    }
}
