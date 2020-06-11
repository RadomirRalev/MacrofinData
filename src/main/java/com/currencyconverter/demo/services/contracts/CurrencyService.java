package com.currencyconverter.demo.services.contracts;

import com.currencyconverter.demo.models.Currency;

import java.util.List;
import java.util.Map;

public interface CurrencyService {
    Map<String, List<Currency>> getAllCurrencies();

    Map<String, Currency> getByCode(String code);

    Map<String, Currency[]> getPairByCode(String code1, String code2);

    String getCurrencyToEuroByCode(String amount, String code);

    String getEuroToCurrencyByCode(String amount, String code);

    String getCurrencyToCurrencyByCode(String amount, String code1, String code2);
}
