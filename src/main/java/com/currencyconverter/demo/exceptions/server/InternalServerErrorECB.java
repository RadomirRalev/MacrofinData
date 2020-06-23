package com.currencyconverter.demo.exceptions.server;

import com.currencyconverter.demo.exceptions.CustomException;
import org.springframework.http.HttpStatus;

import static com.currencyconverter.demo.constants.ExceptionConstants.INTERNAL_SERVER_ERROR_EXCEPTION_HTTP_CODE;

public class InternalServerErrorECB extends CustomException {
    private int errorCode;
    private String message;
    private HttpStatus status;

    private InternalServerErrorECB() {
        status = HttpStatus.INTERNAL_SERVER_ERROR;
        errorCode = INTERNAL_SERVER_ERROR_EXCEPTION_HTTP_CODE;
    }

    public InternalServerErrorECB(String message) {
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
