package com.currencyconverter.demo.repositories.contracts;

import com.currencyconverter.demo.models.mvcmodels.Currency;

import java.util.HashMap;
import java.util.List;

public interface CurrencyRepository {
    Currency getById(int id);

    Currency getByCode(String code);

    void updateRecords(HashMap<String, String> codesAndRates);

    List<Currency> getAllCurrencies();
}
