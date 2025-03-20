package com.locadora.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ModelNotFoundException.class)
    private ResponseEntity<String> modelNotFoundHandler(ModelNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Model not found.");
    }
}
