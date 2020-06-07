package com.currencyconverter.demo.constants;

public class ApiConstants {
    //Navigate JSON Data
    public static final String KEY_OF_CURRENCY_DATA_ARRAY = "dataSets";
    public static final int INDEX_OF_OBJECT_CONTAINING_EXCHANGE_RATE_SERIES = 0;
    public static final String NAME_OF_OBJECT_CONTAINING_NESTED_EXCHANGE_RATE_OBJECTS = "series";
    public static final String NAME_OF_OBJECT_CONTAINING_DESCRIPTION_OF_DATA = "structure";
    public static final String NAME_OF_OBJECT_CONTAINING_ATTRIBUTES_OF_DATA = "attributes";
    public static final String KEY_OF_CURRENCY_CODES_ARRAY = "series";
    public static final int INDEX_OF_OBJECT_CONTAINING_CURRENCY_CODES_ARRAY = 18;
    public static final String KEY_OF_ARRAY_CONTAINING_THREE_LETTER_CURRENCY_CODE = "values";
    public static final String NAME_OF_OBJECT_CONTAINING_ARRAY_OF_EXCHANGE_RATES = "observations";
    public static final String KEY_OF_ARRAY_CONTAINING_EXCHANGE_RATES = "0";
    public static final int INDEX_OF_OBJECT_CONTAINING_CURRENCY_RATE = 0;
    public static final String KEY_OF_CURRENCY_CODE_IN_OBJECT = "id";

    //Build Queries
    public static final String DATA_FORMAT_IN_URI_STRING = "yyyy-MM-dd";
    public static final String URI_OF_ECB_DATA_BANK = "https://sdw-wsrest.ecb.europa.eu/service/data/";
    public static final String URI_OF_ECB_EXCHANGE_RATE_DATA = "EXR/";
    public static final String URI_TO_GET_DATA_FOR_ALL_CURRENCIES = "D..EUR.SP00.A?";
    public static final String URI_START_PERIOD = "startPeriod=";
    public static final String URI_END_PERIOD = "endPeriod=";
    public static final String URI_AND_OPERATOR = "&";
}