package com.example.Backend.controller;

import com.example.Backend.dto.Response.SearchResponse;
import com.example.Backend.dto.ResponseDto;
import com.example.Backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("")
    public ResponseDto<SearchResponse> searchBlogOrUser(@RequestParam("data") String search) {
        return ResponseDto.success(searchService.searchBlogOrUser(search));
    }
}
