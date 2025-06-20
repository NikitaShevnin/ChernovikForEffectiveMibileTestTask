package com.example.bankcards.exception;

/**
 * Thrown when card status does not allow the requested operation.
 */
public class InvalidCardStatusException extends RuntimeException {
    public InvalidCardStatusException(String message) {
        super(message);
    }
}
