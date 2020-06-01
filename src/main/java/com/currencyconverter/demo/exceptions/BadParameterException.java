package com.currencyconverter.demo.exceptions;

public class BadParameterException extends RuntimeException {
    private String message;

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

}
