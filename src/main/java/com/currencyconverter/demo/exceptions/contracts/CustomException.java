package com.currencyconverter.demo.exceptions.contracts;

public interface CustomException {

    void setMessage(String message);

    int getErrorCode();
}
