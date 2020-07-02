package com.currencyconverter.demo.services.contracts;

import com.currencyconverter.demo.models.CurrencyCollection;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CurrencyService {
    CurrencyCollection getAllCurrencies(String base);

    CurrencyCollection getByCode(String codes, String base);

    Map<String, Map<String, String>> getAvailableCurrencies(boolean ever);

    CurrencyCollection getCurrenciesPerDate(LocalDate localDate, String codes, String base);

    List<CurrencyCollection> getTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, String base, String codes);

    String getCurrencyToEuroByCode(String amount, String code);

    String getEuroToCurrencyByCode(String amount, String code);

    String getCurrencyToCurrencyByCode(String amount, String code1, String code2);
}
