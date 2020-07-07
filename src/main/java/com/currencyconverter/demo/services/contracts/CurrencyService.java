package com.currencyconverter.demo.services.contracts;

import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.models.CurrencyCollection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CurrencyService {
    Map<String, Map<String, String>> getAvailableCurrencies(boolean ever);

    CurrencyCollection getCurrenciesPerSingleDay(LocalDate localDate, ArrayList<String> listOfCurrencyCodes, String base);

    List<CurrencyCollection> getTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, String base, ArrayList<String> listOfCurrencyCodes);
}
