package com.example.Backend.mapper;

import com.example.Backend.dto.Response.BlogResponseDto;
import com.example.Backend.entity.Blog;
import com.example.Backend.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(target = "author", expression = "java(blog.getAuthor().getFullName())")
    @Mapping(target = "likesCount", expression = "java(countLikes(blog))")
    @Mapping(target = "commentsCount", expression = "java(blog.getComments().size())")
    @Mapping(target = "sharesCount", expression = "java(blog.getShares().size())")
    BlogResponseDto toBlogResponseDto(Blog blog);

    default int countLikes(Blog blog) {
        int count = 0;
        List<Like> likes = blog.getLikes();
        for (Like like : likes) {
            if (like.isLiked()) {
                count++;
            }
        }
        return count;
    }
}
