package com.currencyconverter.demo.exceptions.server;

import com.currencyconverter.demo.exceptions.CustomException;
import org.springframework.http.HttpStatus;

import static com.currencyconverter.demo.constants.ExceptionConstants.NO_RESPONSE_EXCEPTION_HTTP_CODE;

public class BadGatewayException extends CustomException {
    private int errorCode;
    private String message;
    private HttpStatus status;

    private BadGatewayException() {
        status = HttpStatus.BAD_GATEWAY;
        errorCode = NO_RESPONSE_EXCEPTION_HTTP_CODE;
    }

    public BadGatewayException(String message) {
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
