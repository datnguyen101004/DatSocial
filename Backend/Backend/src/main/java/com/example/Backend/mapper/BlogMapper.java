package com.example.Backend.mapper;

import com.example.Backend.dto.Response.BlogResponseDto;
import com.example.Backend.entity.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlogMapper {
    @Mapping(target = "author", expression = "java(blog.getAuthor().getFullName())")
    BlogResponseDto toBlogResponseDto(Blog blog);
}
