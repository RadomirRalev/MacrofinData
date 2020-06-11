package com.currencyconverter.demo.exceptions;

import java.time.LocalDateTime;

public class CustomException extends RuntimeException {
    private LocalDateTime timestamp;
    private int errorCode;
    private String message;

    protected CustomException() {
        timestamp = LocalDateTime.now();
    }

    public CustomException(String message) {
        this();
        this.message = message;
    }

    public CustomException(int errorCode, String message) {
        this();
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
