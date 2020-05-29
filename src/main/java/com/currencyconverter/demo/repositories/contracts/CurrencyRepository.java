package com.currencyconverter.demo.repositories.contracts;

import com.currencyconverter.demo.models.Currency;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public interface CurrencyRepository {
    Currency getById(int id);

    Currency getByCode(String code);

    void updateRecords(HashMap<String, String> codesAndRates);

    List<Currency> getAllCurrencies();
}
