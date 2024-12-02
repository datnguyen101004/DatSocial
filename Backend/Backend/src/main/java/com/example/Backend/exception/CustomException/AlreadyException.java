package com.example.Backend.exception.CustomException;

public class AlreadyException extends RuntimeException {
    public AlreadyException(String message) {
        super(message);
    }
}
