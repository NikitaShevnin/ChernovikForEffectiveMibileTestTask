package com.example.bankcards.exception;

/**
 * Simple DTO used to return error information in API responses.
 */

import java.time.LocalDateTime;
public class ApiError {
    private LocalDateTime timestamp;
    private String message;

    public ApiError(LocalDateTime timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
