package com.example.exceptions.controllerAdvices;

import com.example.exceptions.RoleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RoleNotFoundExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String roleNotFoundHandler(RoleNotFoundException ex) {
        return ex.getMessage();
    }
}
