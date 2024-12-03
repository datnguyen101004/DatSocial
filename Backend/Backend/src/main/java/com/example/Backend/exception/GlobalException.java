package com.example.Backend.exception;

import com.example.Backend.dto.ResponseDto;
import com.example.Backend.exception.CustomException.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<?>> emailExist(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ResponseDto<?>> invalidCredential(InvalidCredentialException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.fail(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ResponseDto<?>> invalidJwtToken(InvalidJwtTokenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDto.fail(HttpStatus.FORBIDDEN.value(), e.getMessage()));
    }

    @ExceptionHandler(AlreadyException.class)
    public ResponseEntity<ResponseDto<?>> alreadyException(AlreadyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(HttpStatus.CONFLICT.value(), e.getMessage()));
    }

    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<ResponseDto<?>> unauthorizedException(NotPermissionException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.fail(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }
}
