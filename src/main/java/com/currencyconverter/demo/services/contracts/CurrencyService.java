package com.currencyconverter.demo.services.contracts;

import com.currencyconverter.demo.models.CurrencyCollection;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface CurrencyService {
    CurrencyCollection getAllCurrencies();

    CurrencyCollection getByCode(String codes);

    Map<String, Map<String, String>> getAvailableCurrencies();

    CurrencyCollection getCurrenciesPerDate(String date);

    List<CurrencyCollection> getTimeSeries(String from, String to, String page, String limit);

    String getCurrencyToEuroByCode(String amount, String code);

    String getEuroToCurrencyByCode(String amount, String code);

    String getCurrencyToCurrencyByCode(String amount, String code1, String code2);
}
