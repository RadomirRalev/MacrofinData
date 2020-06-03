package com.currencyconverter.demo.exceptions;

public class BadParameterException extends RuntimeException {
    private String message;
    public static int ERROR_CODE = 400;

    public BadParameterException() {
    }

    public BadParameterException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static int getErrorCode() {
        return ERROR_CODE;
    }

}
