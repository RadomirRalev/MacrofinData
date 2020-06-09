package com.currencyconverter.demo.api;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.currencyconverter.demo.constants.ApiConstants.*;

public class QueryBuilder {

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

    public static String formatTodayDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATA_FORMAT_IN_URI_STRING);
        if (LOCAL_TIME_TO_CET.isAfter(CURRENCY_RATE_UPDATE_TIME)) {
            LocalDateTime todayDate = LocalDateTime.now();
            return dtf.format(todayDate);
        }
        return dtf.format(getYesterdayDate());
    }

    private static LocalDateTime getYesterdayDate() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        return LocalDateTime.ofInstant(yesterday, ZoneOffset.UTC);
    }
}
