package com.currencyconverter.demo.services.restservices.contracts;

import com.currencyconverter.demo.models.mvcmodels.Currency;
import com.currencyconverter.demo.models.restmodels.CurrencyRest;

import java.util.List;
import java.util.Map;

public interface CurrencyServiceRest {
    Map<String, List<CurrencyRest>> getAllCurrencies();

    Currency getById(int id);

    Map<String, CurrencyRest> getByCode(String code);

    Currency[] getPairById(int id1, int id2);

    Currency[] getPairByCode(String code1, String code2);

    String getCurrencyToEuroByCode(String amount, String code);

    String getEuroToCurrencyByCode(String amount, String code);

    String getCurrencyToCurrencyByCode(String amount, String code1, String code2);
}
