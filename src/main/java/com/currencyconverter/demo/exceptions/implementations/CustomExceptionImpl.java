package com.currencyconverter.demo.exceptions.implementations;

import com.currencyconverter.demo.exceptions.contracts.CustomException;

public class CustomExceptionImpl extends RuntimeException implements CustomException {
    int errorCode;
    String message;

    public CustomExceptionImpl() {

    }

    public CustomExceptionImpl(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public void setMessage(String message) {

    }

    @Override
    public int getErrorCode() {
        return 0;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
