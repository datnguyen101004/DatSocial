package com.example.Backend.exception.CustomException;

public class NotPermissionException extends RuntimeException {
    public NotPermissionException(String message) {
        super(message);
    }
}
