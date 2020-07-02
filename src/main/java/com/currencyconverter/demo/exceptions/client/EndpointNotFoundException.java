package com.currencyconverter.demo.exceptions.client;

import com.currencyconverter.demo.exceptions.CustomException;
import org.springframework.http.HttpStatus;

import static com.currencyconverter.demo.constants.ExceptionConstants.WRONG_ENDPOINT_EXCEPTION_HTTP_CODE;

public class EndpointNotFoundException extends CustomException {
    private int errorCode;
    private HttpStatus status;
    private String message;
    private String currencyCode;

    private EndpointNotFoundException() {
        status = HttpStatus.NOT_FOUND;
        errorCode = WRONG_ENDPOINT_EXCEPTION_HTTP_CODE;
    }

    public EndpointNotFoundException(String message) {
        this();
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
