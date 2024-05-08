package com.example.exceptions;

public class NotAllowedDeleteOperationException extends RuntimeException {

    public NotAllowedDeleteOperationException(String message) {
        super(message);
    }
}
