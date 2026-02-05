package com.catijr.peugeot.exceptions;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("User not found with id: " + id);
    }
}