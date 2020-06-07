package com.currencyconverter.demo.exceptions.implementations;

import static com.currencyconverter.demo.constants.ExceptionConstants.BAD_PARAMETER_EXCEPTION_HTTP_CODE;

public class BadParameterException extends CustomExceptionImpl {
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

    public int getErrorCode() {
        return BAD_PARAMETER_EXCEPTION_HTTP_CODE;
    }

}
