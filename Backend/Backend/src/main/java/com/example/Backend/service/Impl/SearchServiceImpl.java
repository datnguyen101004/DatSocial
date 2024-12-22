package com.example.Backend.service.Impl;

import com.example.Backend.dto.Response.BlogResponseDto;
import com.example.Backend.dto.Response.SearchResponse;
import com.example.Backend.dto.Response.SearchUserResponse;
import com.example.Backend.entity.Blog;
import com.example.Backend.entity.User;
import com.example.Backend.mapper.BlogMapper;
import com.example.Backend.repository.BlogRepository;
import com.example.Backend.repository.UserRepository;
import com.example.Backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    @Override
    public SearchResponse searchBlogOrUser(String search) {
        List<User> search1 = userRepository.searchUsers(search);
        List<Blog> search2 = blogRepository.searchBlogs(search);
        return SearchResponse.builder()
                .searchUserResponses(search1.stream().map(user -> SearchUserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .build()).toList())
                .blogResponseDtos(search2.stream().map(blogMapper::toBlogResponseDto).toList())
                .build();
    }
}
