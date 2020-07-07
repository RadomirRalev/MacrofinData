package com.currencyconverter.demo.services.contracts.ecb;

import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.models.CurrencyCollection;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CurrencyServiceECB {

    Map<String, Map<String, String>> getAvailableCurrencies(boolean ever);

    ArrayList<Currency> getCurrenciesPerSingleDay(LocalDate localDate, ArrayList<String> listOfCurrencyCodes, String base);

    ArrayList<ArrayList<Currency>> getTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, String base, ArrayList<String> listOfCurrencyCodes);

}
