package com.example.exceptions.controllerAdvices;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class NonValidRequestHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpClientErrorException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String nonValidRequestHandler(BindException ex) {
        FieldError fieldError = ex.getFieldError();
        if (fieldError != null) {
            String defaultMessage = fieldError.getDefaultMessage();
            System.out.println("Error: " + defaultMessage);
            return defaultMessage;
        }
        return ex.getMessage();
    }
}
