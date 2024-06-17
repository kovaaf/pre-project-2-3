package com.example.exceptions.controllerAdvices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class TooManyRequestsHandler {
    @ExceptionHandler({
            HttpClientErrorException.TooManyRequests.class,
            HttpClientErrorException.Forbidden.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    String tooManyRequestsHandler(Exception ex) {
        System.out.println("Error: " + ex.getMessage());
        return "Internal error. Too many requests to external API.\n" + ex.getMessage();
    }
}
