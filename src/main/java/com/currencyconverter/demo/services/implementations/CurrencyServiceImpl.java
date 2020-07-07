package com.currencyconverter.demo.services.implementations;

import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import com.currencyconverter.demo.services.contracts.ecb.CurrencyServiceECB;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyServiceECB currencyServiceECB;

    public CurrencyServiceImpl(CurrencyServiceECB currencyServiceECB) {
        this.currencyServiceECB = currencyServiceECB;
    }

    @Override
    public Map<String, Map<String, String>> getAvailableCurrencies(boolean ever) {
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        availableCurrencies.putAll(currencyServiceECB.getAvailableCurrencies(ever));
        return availableCurrencies;
    }

    @Override
    public CurrencyCollection getCurrenciesPerSingleDay(LocalDate localDate, ArrayList<String> listOfCurrencyCodes, String base) {
        ArrayList<Currency> ecbCurrencies = currencyServiceECB.getCurrenciesPerSingleDay(localDate, listOfCurrencyCodes, base);
        ArrayList<Currency> result = new ArrayList<>();
        result.addAll(ecbCurrencies);
        return new CurrencyCollection(localDate.toString(), result, base.toUpperCase());
    }

    @Override
    public List<CurrencyCollection> getTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, String base, ArrayList<String> listOfCurrencyCodes) {
        ArrayList<ArrayList<Currency>> listOfCurrencies = new ArrayList<>();
        ArrayList<ArrayList<Currency>> listOfECBCurrencies = currencyServiceECB.getTimeSeries(localDateFrom, localDateTo, page, limit, base, listOfCurrencyCodes);
        listOfCurrencies.addAll(listOfECBCurrencies);
        List<CurrencyCollection> result = new ArrayList<>();
        for (int i = 0; i < listOfCurrencies.size(); i++) {
            CurrencyCollection collection = new CurrencyCollection(localDateFrom.toString(), listOfCurrencies.get(i), base.toUpperCase());
            result.add(collection);
            localDateFrom = localDateFrom.plusDays(1);
        }
        return result;
    }
}
