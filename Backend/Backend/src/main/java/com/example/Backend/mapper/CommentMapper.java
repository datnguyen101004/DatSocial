package com.example.Backend.mapper;

import com.example.Backend.dto.Response.CommentResponse;
import com.example.Backend.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "commentId", source = "id")
    @Mapping(target = "userId", expression = "java(comment.getUser().getId())")
    @Mapping(target = "fullName", expression = "java(comment.getUser().getFullName())")
    @Mapping(target = "blogId", expression = "java(comment.getBlog().getId())")
    CommentResponse toCommentResponse(Comment comment);
}
