package com.currencyconverter.demo.constants;

public class ExceptionConstants {
    //status codes
    public static final int BAD_PARAMETER_EXCEPTION_HTTP_CODE = 400;
    public static final int WRONG_ENDPOINT_EXCEPTION_HTTP_CODE = 404;
    public static final int INVALID_BASE_CURRENCY_EXCEPTION = 422;
    public static final int INTERNAL_SERVER_ERROR_EXCEPTION_HTTP_CODE = 500;
    public static final int NO_RESPONSE_EXCEPTION_HTTP_CODE = 502;

    //400
    public static final String BAD_PARAMETER_ID_NOT_NUMBER = "Bad parameter! Id must be a number!";
    public static final String BAD_PARAMETER_ID_LESS_THAN_ZERO = "Bad parameter! Id must be zero or a positive number!";
    public static final String BAD_PARAMETER_ISO_INCORRECT_NUMBER_LETTERS = "Bad parameter! ISO code must contain exactly three letters!";
    public static final String BAD_PARAMETER_ISO_NO_LETTERS = "Bad parameter! ISO code must contain only letters!";
    public static final String AMOUNT_NO_INTEGER = "Bad parameter! Amount must be an integer or decimal number!";
    public static final String BAD_PARAMETER_WRONG_DATE_FORMAT = "Bad Parameter! Wrong date format! Date format must be 'yyyy-mm-dd'";

    //404
    public static final String WRONG_ENDPOINT_EXCEPTION_MESSAGE = "The requested API endpoint does not exist!";
    public static final String NO_SUCH_CODE_EXISTS_IN_THE_LIST_OF_ECB_CURRENCIES = " - No such code exists in the list of available ECB currencies";

    //422
    public static final String INVALID_DATE_AFTER = " is after today!";
    public static final String INVALID_DATE_BEFORE = " is before 1999-01-01!";
    public static final String INVALID_DATE_FROM_DATE_AFTER_TO_DATE = " - 'From' date should be before 'To' date";
    public static final String INVALID_PAGE_LIMIT_NUMBER = " - 'Page' must be an integer larger than 1";
    public static final String INVALID_PAGE_NUMBER_HIGHER_THAN_SELECTION = " - 'Page' exceeds number of currencies";
    public static final String INVALID_LIMIT_NUMBER = " - 'Limit' must be an integer between 1 and 100";


    //500
    public static final String UNVALID_RESPONSE_ECB_SERVER = "ECB server returns unvalid response!";

    //502
    public static final String NO_CONNECTION_WITH_ECB_SERVER = "ECB server returns unvalid response! This may be caused by unresolved host or internal server error";




    public static final String BAD_REQUEST_IN_JSON_FILE_WRONG_KEY = " key does not exist in JSON file! Cannot process document!";
    public static final String BAD_REQUEST_IN_JSON_FILE_WRONG_INDEX = " index does not exist in JSON file! Cannot process document!";
    public static final String BAD_REQUEST_IN_JSON_FILE_WRONG_NAME = " name of object does not exist in JSON file! Cannot process document!";
    public static final String BAD_REQUEST_IN_JSON_FILE_NO_VALUES = "No values to process in JSON Array at index: ";
    public static final String BAD_REQUEST_JSON_ARRAY_IS_EMPTY = "JSON Array is empty!";






}
