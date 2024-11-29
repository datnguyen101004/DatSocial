package com.example.Backend.dto;

import com.example.Backend.dto.Request.RegisterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ResponseDto<T> success(T data){
        return ResponseDto.<T>builder()
                .status(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static ResponseDto<Void> fail(int status, String message){
        return ResponseDto.<Void>builder()
                .status(status)
                .message(message)
                .data(null)
                .build();
    }
}
