package com.currencyconverter.demo.exceptions.client;
import com.currencyconverter.demo.exceptions.CustomException;
import org.springframework.http.HttpStatus;

import static com.currencyconverter.demo.constants.ExceptionConstants.WRONG_URL_EXCEPTION_HTTP_CODE;

public class ResourceNotFoundException extends CustomException {
    private int errorCode;
    private HttpStatus status;
    private String message;
    private String currencyCode;

    private ResourceNotFoundException() {
        status = HttpStatus.NOT_FOUND;
        errorCode = WRONG_URL_EXCEPTION_HTTP_CODE;
    }

    public ResourceNotFoundException(String message) {
        this();
        this.message = message;
    }

    public ResourceNotFoundException(String message, String currencyCode) {
        this();
        this.message = currencyCode.toUpperCase() + " - " + message;
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
