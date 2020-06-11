package com.currencyconverter.demo.exceptions;

import org.springframework.http.HttpStatus;

import static com.currencyconverter.demo.constants.ExceptionConstants.BAD_PARAMETER_EXCEPTION_HTTP_CODE;

public class BadParameterException extends CustomException {
    private int errorCode;
    private String message;
    private HttpStatus status;

    private BadParameterException() {
        status = HttpStatus.BAD_REQUEST;
        errorCode = BAD_PARAMETER_EXCEPTION_HTTP_CODE;
    }

    public BadParameterException(String message) {
        this();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
