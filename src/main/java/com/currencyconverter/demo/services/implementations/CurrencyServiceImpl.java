package com.currencyconverter.demo.services.implementations;

import com.currencyconverter.demo.exceptions.ResourceNotFoundException;
import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.currencyconverter.demo.api.JsonDataNavigator.*;
import static com.currencyconverter.demo.api.QueryBuilder.formatTodayDate;
import static com.currencyconverter.demo.constants.ExceptionConstants.NO_SUCH_CODE_EXISTS_IN_THE_LIST_OF_ECB_CURRENCIES;
import static com.currencyconverter.demo.helpers.DataProcessor.processKeysInCurrencyRateObject;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Override
    public Map<String, List<Currency>> getAllCurrencies() {
        Map<String, List<Currency>> resultMap = new HashMap<>();
        String date = formatTodayDate();
        resultMap.put(date, getListOfAllCurrencies());
        return resultMap;
    }

    @Override
    public Map<String, Currency> getByCode(String code) {
        Map<String, Currency> resultMap = new HashMap<>();
        String date = formatTodayDate();
        JSONArray values = getArrayOfCurrencyCodeObjects();
        resultMap.put(date, getCurrencyRestObject(code, values));
        return resultMap;
    }

    @Override
    public Map<String, Currency[]> getPairByCode(String code1, String code2) {
        Map<String, Currency[]> resultMap = new HashMap<>();
        String date = formatTodayDate();
        JSONArray values = getArrayOfCurrencyCodeObjects();
        Currency[] resultArray = {getCurrencyRestObject(code1, values), getCurrencyRestObject(code2, values)};
        resultMap.put(date, resultArray);
        return resultMap;
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

    private static ArrayList<String> getDailyExchangeRates() {
        Map<Integer, JSONObject> jsonObjectHashMap = processKeysInCurrencyRateObject();
        ArrayList<String> arrOfRates = new ArrayList<>();
        for (int i = 0; i < jsonObjectHashMap.size(); i++) {
            arrOfRates.add(getCurrencyExchangeRate(jsonObjectHashMap, i));
        }
        return arrOfRates;
    }

    private static Currency getCurrencyRestObject(String code, JSONArray values) {
        Currency currency1 = new Currency();
        if (getEuroIfCodeIsEur(code, currency1)) {
            return currency1;
        }
        int index1 = getIndexOfSingleCurrency(code, values);
        currency1.setCode(code.toUpperCase());
        currency1.setCurrencyName(getCurrencyNameString(values, index1));
        currency1.setValue(getDailyExchangeRates(index1));
        return currency1;
    }

    private static boolean getEuroIfCodeIsEur(String code, Currency currency1) {
        if (code.equalsIgnoreCase("eur")) {
            currency1.setValue("1.0");
            currency1.setCurrencyName("Euro");
            currency1.setCode("EUR");
            return true;
        }
        return false;
    }

    private static String getDailyExchangeRates(int index) {
        Map<Integer, JSONObject> jsonObjectHashMap = processKeysInCurrencyRateObject();
        return getCurrencyExchangeRate(jsonObjectHashMap, index);
    }

    private static ArrayList[] getCurrencyCodesAndNames() {
        ArrayList<String> currencyCodes = new ArrayList<>();
        ArrayList<String> currencyNames = new ArrayList<>();
        JSONArray values = getArrayOfCurrencyCodeObjects();
        for (int i = 0; i < values.length(); i++) {
            currencyCodes.add(getCurrencyCodeString(values, i));
            currencyNames.add(getCurrencyNameString(values, i));
        }
        return new ArrayList[] {currencyCodes, currencyNames};
    }

    private static List<Currency> getListOfAllCurrencies() {
        List<Currency> codesAndRates = new ArrayList<>();
        ArrayList[] codes = getCurrencyCodesAndNames();
        ArrayList<String> rates = getDailyExchangeRates();
        for (int i = 0; i < rates.size(); i++) {
            Currency currency = new Currency();
            currency.setCode(codes[0].get(i).toString());
            currency.setValue(rates.get(i));
            currency.setCurrencyName(codes[1].get(i).toString());
            codesAndRates.add(currency);
        }
        return codesAndRates;
    }

    private static int getIndexOfSingleCurrency(String code, JSONArray values) {
        int index = -1;
        for (int i = 0; i < values.length(); i++) {
            if (getCurrencyCodeString(values, i).equalsIgnoreCase(code)) {
                index = i;
                break;
            }
            if (i == values.length() - 1) {
                throw new ResourceNotFoundException(NO_SUCH_CODE_EXISTS_IN_THE_LIST_OF_ECB_CURRENCIES, code);
            }
        }
        return index;
    }
}
