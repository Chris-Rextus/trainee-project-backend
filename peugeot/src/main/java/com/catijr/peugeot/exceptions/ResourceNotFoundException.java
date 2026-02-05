package com.catijr.peugeot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Class<?> entityClass, Object id) {
        super(String.format("%s not found with id: %s",
                entityClass.getSimpleName(), id));
    }

    public ResourceNotFoundException(Class<?> entityClass, String field, Object value) {
        super(String.format("%s not found with %s: %s",
                entityClass.getSimpleName(), field, value));
    }
}