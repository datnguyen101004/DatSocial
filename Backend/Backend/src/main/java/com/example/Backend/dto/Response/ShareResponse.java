package com.example.Backend.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareResponse {
    private String message;
    private BlogResponseDto blogResponseDto;
    private String displayZone;
}
