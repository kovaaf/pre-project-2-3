package com.example.exceptions.controllerAdvices;

import com.example.exceptions.NotAllowedDeleteOperationException;
import com.example.exceptions.RoleNotFoundException;
import com.example.exceptions.UserNotFoundException;
import com.example.exceptions.UsernameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BadRequestHandler {

    @ExceptionHandler({
            NotAllowedDeleteOperationException.class,
            RoleNotFoundException.class,
            UsernameAlreadyTakenException.class,
            UserNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleBadRequest(Exception ex) {
        return ex.getMessage();
    }
}
