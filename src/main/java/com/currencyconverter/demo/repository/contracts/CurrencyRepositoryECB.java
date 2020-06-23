package com.currencyconverter.demo.repository.contracts;

import com.currencyconverter.demo.models.CurrencyCollection;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CurrencyRepositoryECB extends CurrencyRepository {

    CurrencyCollection getAllCurrencies();

    CurrencyCollection getByCode(String codes);

    Map<String, Map<String, String>> getAvailableCurrencies();

    CurrencyCollection getCurrenciesPerDate(LocalDate localDate);

    List<CurrencyCollection> getTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit);

    String getCurrencyToEuroByCode(String amount, String code);

    String getEuroToCurrencyByCode(String amount, String code);

    String getCurrencyToCurrencyByCode(String amount, String code1, String code2);
}
