package com.example.exceptions.controllerAdvices;

import com.example.exceptions.NotAllowedDeleteOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotAllowedDeleteOperationExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(NotAllowedDeleteOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String roleNotFoundHandler(NotAllowedDeleteOperationException ex) {
        return ex.getMessage();
    }
}
