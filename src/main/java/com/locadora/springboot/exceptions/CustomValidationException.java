package com.locadora.springboot.exceptions;

public class CustomValidationException extends RuntimeException{
    public CustomValidationException(String message){
        super(message);
    }
}
