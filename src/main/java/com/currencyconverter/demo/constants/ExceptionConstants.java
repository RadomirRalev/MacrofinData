package com.currencyconverter.demo.constants;

public class ExceptionConstants {
    //status codes
    public static final int BAD_PARAMETER_EXCEPTION_HTTP_CODE = 400;
    public static final int WRONG_URL_EXCEPTION_HTTP_CODE = 404;

    //400
    public static final String BAD_PARAMETER_ID_NOT_NUMBER = "Bad parameter! Id must be a number!";
    public static final String BAD_PARAMETER_ID_LESS_THAN_ZERO = "Bad parameter! Id must be zero or a positive number!";
    public static final String BAD_PARAMETER_ISO_INCORRECT_NUMBER_LETTERS = "Bad parameter! ISO code must contain exactly three letters!";
    public static final String BAD_PARAMETER_ISO_NO_LETTERS = "Bad parameter! ISO code must contain only letters!";
    public static final String AMOUNT_NO_INTEGER = "Bad parameter! Amount must be an integer or decimal number!";

    //404
    public static final String WRONG_URL_EXCEPTION_MESSAGE = "Wrong URL! Please check api documentation!";

}
