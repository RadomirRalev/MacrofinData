package com.currencyconverter.demo.exceptions.client;

import com.currencyconverter.demo.exceptions.CustomException;
import org.springframework.http.HttpStatus;

import static com.currencyconverter.demo.constants.ExceptionConstants.INVALID_BASE_CURRENCY_EXCEPTION;

public class UnprocessableEntityException extends CustomException {
    private int errorCode;
    private HttpStatus status;
    private String message;

    protected UnprocessableEntityException() {
        status = HttpStatus.UNPROCESSABLE_ENTITY;
        errorCode = INVALID_BASE_CURRENCY_EXCEPTION;
    }

    public UnprocessableEntityException(String message) {
        this();
        this.message = message;
    }

    public UnprocessableEntityException(String message, String entity) {
        this();
        this.message = entity + " " + message;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    private void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    private void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }
}
