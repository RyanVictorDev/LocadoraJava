package com.locadora.springboot.exceptions;

public class ModelNotFoundException extends RuntimeException{
    public ModelNotFoundException() { super("Model not found!"); }

    public ModelNotFoundException(String message){ super(message); }
}
