package com.example.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("User " + userId + " not found in DB!");
    }
}
