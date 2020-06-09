package com.currencyconverter.demo.services.restservices.implementations;

import com.currencyconverter.demo.models.mvcmodels.Currency;
import com.currencyconverter.demo.models.restmodels.CurrencyRest;
import com.currencyconverter.demo.services.restservices.contracts.CurrencyServiceRest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.currencyconverter.demo.api.JsonDataNavigator.*;
import static com.currencyconverter.demo.api.QueryBuilder.formatTodayDate;
import static com.currencyconverter.demo.helpers.DataProcessor.processKeysInCurrencyRateObject;

@Service
public class CurrencyServiceRestImpl implements CurrencyServiceRest {

    @Override
    public Map<String, List<CurrencyRest>> getAllCurrencies() {
        Map<String, List<CurrencyRest>> resultMap = new HashMap<>();
        String date = formatTodayDate();
        resultMap.put(date, getListOfAllCurrencies());
        return resultMap;
    }

    @Override
    public Currency getById(int id) {
        return null;
    }

    @Override
    public Map<String, CurrencyRest> getByCode(String code) {
        Map<String, CurrencyRest> resultMap = new HashMap<>();
        String date = formatTodayDate();
        CurrencyRest currencyRest = new CurrencyRest();
        JSONArray values = getArrayOfCurrencyCodeObjects();
        int index = getIndexOfSingleCurrency(code, values);
        currencyRest.setCode(code.toUpperCase());
        currencyRest.setCurrencyName(getCurrencyNameString(values, index));
        currencyRest.setValue(getDailyExchangeRates(index));
        resultMap.put(date, currencyRest);
        return resultMap;
    }

    @Override
    public Currency[] getPairById(int id1, int id2) {
        return new Currency[0];
    }

    @Override
    public Currency[] getPairByCode(String code1, String code2) {
        return new Currency[0];
    }

    @Override
    public String getCurrencyToEuroByCode(String amount, String code) {
        return null;
    }

    @Override
    public String getEuroToCurrencyByCode(String amount, String code) {
        return null;
    }

    @Override
    public String getCurrencyToCurrencyByCode(String amount, String code1, String code2) {
        return null;
    }

    private ArrayList<String> getDailyExchangeRates() {
        Map<Integer, JSONObject> jsonObjectHashMap = processKeysInCurrencyRateObject();
        ArrayList<String> arrOfRates = new ArrayList<>();
        for (int i = 0; i < jsonObjectHashMap.size(); i++) {
            arrOfRates.add(getCurrencyExchangeRate(jsonObjectHashMap, i));
        }
        return arrOfRates;
    }

    private String getDailyExchangeRates(int index) {
        Map<Integer, JSONObject> jsonObjectHashMap = processKeysInCurrencyRateObject();
        return getCurrencyExchangeRate(jsonObjectHashMap, index);
    }

    private ArrayList[] getCurrencyCodesAndNames() {
        ArrayList<String> currencyCodes = new ArrayList<>();
        ArrayList<String> currencyNames = new ArrayList<>();
        JSONArray values = getArrayOfCurrencyCodeObjects();
        for (int i = 0; i < values.length(); i++) {
            currencyCodes.add(getCurrencyCodeString(values, i));
            currencyNames.add(getCurrencyNameString(values, i));
        }
        return new ArrayList[] {currencyCodes, currencyNames};
    }

    private List<CurrencyRest> getListOfAllCurrencies() {
        List<CurrencyRest> codesAndRates = new ArrayList<>();
        ArrayList[] codes = getCurrencyCodesAndNames();
        ArrayList<String> rates = getDailyExchangeRates();
        for (int i = 0; i < rates.size(); i++) {
            CurrencyRest currency = new CurrencyRest();
            currency.setCode(codes[0].get(i).toString());
            currency.setValue(rates.get(i));
            currency.setCurrencyName(codes[1].get(i).toString());
            codesAndRates.add(currency);
        }
        return codesAndRates;
    }

    private int getIndexOfSingleCurrency(String code, JSONArray values) {
        int index = -1;
        for (int i = 0; i < values.length(); i++) {
            if (getCurrencyCodeString(values, i).equalsIgnoreCase(code)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
