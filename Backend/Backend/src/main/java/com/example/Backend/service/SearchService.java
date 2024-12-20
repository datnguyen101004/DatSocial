package com.example.Backend.service;

import com.example.Backend.dto.Response.SearchResponse;

public interface SearchService {
    SearchResponse searchBlogOrUser(String search);
}
