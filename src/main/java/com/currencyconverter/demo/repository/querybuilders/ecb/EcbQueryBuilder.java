package com.currencyconverter.demo.repository.querybuilders.ecb;

import static com.currencyconverter.demo.constants.ApiConstants.*;
import static com.currencyconverter.demo.helpers.DateFormatter.formatDate;
import static com.currencyconverter.demo.helpers.DateFormatter.formatTodayDate;

public class EcbQueryBuilder {

    public static String getCurrencyRatesURI() {
        StringBuilder sb = new StringBuilder();
        sb.append(URI_OF_ECB_DATA_BANK);
        sb.append(URI_OF_ECB_EXCHANGE_RATE_DATA);
        sb.append(URI_TO_GET_DATA_FOR_CURRENCIES).append(URI_TO_GET_DATA_FOR_ALL_CURRENCIES);
        sb.append(URI_START_PERIOD);
        sb.append(formatDate(formatTodayDate()));
        sb.append(URI_AND_OPERATOR);
        sb.append(URI_END_PERIOD);
        sb.append(formatDate(formatTodayDate()));
        return sb.toString();
    }

    public static String getCurrencyRatesURI(String date) {
        StringBuilder sb = new StringBuilder();
        sb.append(URI_OF_ECB_DATA_BANK);
        sb.append(URI_OF_ECB_EXCHANGE_RATE_DATA);
        sb.append(URI_TO_GET_DATA_FOR_CURRENCIES).append(URI_TO_GET_DATA_FOR_ALL_CURRENCIES);
        sb.append(URI_START_PERIOD);
        sb.append(formatDate(date));
        sb.append(URI_AND_OPERATOR);
        sb.append(URI_END_PERIOD);
        sb.append(formatDate(date));
        return sb.toString();
    }

    public static String getCurrencyRatesURI(String from, String to) {
        StringBuilder sb = new StringBuilder();
        sb.append(URI_OF_ECB_DATA_BANK);
        sb.append(URI_OF_ECB_EXCHANGE_RATE_DATA);
        sb.append(URI_TO_GET_DATA_FOR_CURRENCIES).append(URI_TO_GET_DATA_FOR_ALL_CURRENCIES);
        sb.append(URI_START_PERIOD);
        sb.append(formatDate(from));
        sb.append(URI_AND_OPERATOR);
        sb.append(URI_END_PERIOD);
        sb.append(formatDate(to));
        return sb.toString();
    }
}
