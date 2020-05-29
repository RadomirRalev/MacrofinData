package com.currencyconverter.demo.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class QueryBuilder {
    public static final String DATA_FORMAT_IN_URI_STRING = "yyyy-MM-dd";
    public static final String URI_OF_ECB_DATA_BANK = "https://sdw-wsrest.ecb.europa.eu/service/data/";
    public static final String URI_OF_ECB_EXCHANGE_RATE_DATA = "EXR/";
    public static final String URI_TO_GET_DATA_FOR_ALL_CURRENCIES = "D..EUR.SP00.A?";
    public static final String URI_START_PERIOD = "startPeriod=";
    public static final String URI_END_PERIOD = "endPeriod=";
    public static final String URI_AND_OPERATOR = "&";

    public static String getTodayCurrencyRatesURI() {
        StringBuilder sb = new StringBuilder();
        sb.append(URI_OF_ECB_DATA_BANK);
        sb.append(URI_OF_ECB_EXCHANGE_RATE_DATA);
        sb.append(URI_TO_GET_DATA_FOR_ALL_CURRENCIES);
        sb.append(URI_START_PERIOD);
        sb.append(formatTodayDate());
        sb.append(URI_AND_OPERATOR);
        sb.append(URI_END_PERIOD);
        sb.append(formatTodayDate());
        return sb.toString();
    }

    private static String formatTodayDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATA_FORMAT_IN_URI_STRING);
        LocalDateTime todayDate = LocalDateTime.now();
        return dtf.format(todayDate);
    }
}
