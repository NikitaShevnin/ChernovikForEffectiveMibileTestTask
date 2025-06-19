package com.example.bankcards.exception;

/**
 * Thrown when a requested resource is not found in the system.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
