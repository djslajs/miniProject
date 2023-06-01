package com.miniproject.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class ChoException extends RuntimeException{
    private final Map< String, String> validation = new HashMap<>();


    public ChoException(String message) {
        super(message);
    }

    public ChoException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put( fieldName, message);
    }
}
