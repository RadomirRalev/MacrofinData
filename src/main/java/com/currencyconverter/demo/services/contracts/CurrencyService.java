package com.currencyconverter.demo.services.contracts;

import com.currencyconverter.demo.models.Currency;
import java.util.List;

public interface CurrencyService {
    List<Currency> getAllCurrencies();

    Currency getById(int id);

    Currency getByCode(String code);

    Currency[] getPairById(int id1, int id2);

    Currency[] getPairByCode(String code1, String code2);

    String getCurrencyToEuroByCode(String amount, String code);

    String getEuroToCurrencyByCode(String amount, String code);

    String getCurrencyToCurrencyByCode(String amount, String code1, String code2);

    void updateRecords();
}
