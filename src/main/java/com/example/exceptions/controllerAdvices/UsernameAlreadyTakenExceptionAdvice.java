package com.example.exceptions.controllerAdvices;

import com.example.exceptions.UsernameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UsernameAlreadyTakenExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(UsernameAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String usernameAlreadyTakenHandler(UsernameAlreadyTakenException ex) {
        return ex.getMessage();
    }
}
