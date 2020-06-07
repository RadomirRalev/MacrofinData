package com.currencyconverter.demo.exceptions.implementations;
import static com.currencyconverter.demo.constants.ExceptionConstants.WRONG_URL_EXCEPTION_HTTP_CODE;

public class WrongURLException extends CustomExceptionImpl {
    private String message;

    public WrongURLException() {
    }

    public WrongURLException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return WRONG_URL_EXCEPTION_HTTP_CODE;
    }
}
