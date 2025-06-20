package com.example.bankcards.exception;

/**
 * Thrown when there are not enough funds on the card for the operation.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
