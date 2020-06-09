package com.currencyconverter.demo.services.mvcservices.implementations;

import com.currencyconverter.demo.models.mvcmodels.Currency;
import com.currencyconverter.demo.repositories.contracts.CurrencyRepository;
import com.currencyconverter.demo.services.mvcservices.contracts.CurrencyService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import static com.currencyconverter.demo.api.JsonDataNavigator.*;
import static com.currencyconverter.demo.helpers.DataProcessor.processKeysInCurrencyRateObject;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    @Scheduled(cron = "0 19 17 * * ?")
    public void updateRecords() {
        ArrayList<String> codes = getCurrencyCodes();
        ArrayList<String> rates = getDailyExchangeRates();
        HashMap<String, String> codesAndRates = new HashMap<>();
        for (int i = 0; i < codes.size(); i++) {
            codesAndRates.put(codes.get(i), rates.get(i));
        }
        currencyRepository.updateRecords(codesAndRates);
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.getAllCurrencies();
    }

    @Override
    public Currency getById(int id) {
        return currencyRepository.getById(id);
    }

    @Override
    public Currency getByCode(String code) {
        return currencyRepository.getByCode(code);
    }

    @Override
    public Currency[] getPairById(int id1, int id2) {
        return new Currency[]{getById(id1), getById(id2)};
    }

    @Override
    public Currency[] getPairByCode(String code1, String code2) {
        return new Currency[]{getByCode(code1), getByCode(code2)};
    }

    @Override
    public String getCurrencyToEuroByCode(String amount, String code) {
        Currency currency = getByCode(code);
        BigDecimal rateBigDec = new BigDecimal(currency.getValue());
        BigDecimal amountBigDec = new BigDecimal(amount);
        return amountBigDec.divide(rateBigDec, 5, BigDecimal.ROUND_HALF_EVEN).toString();
    }

    @Override
    public String getEuroToCurrencyByCode(String amount, String code) {
        Currency currency = getByCode(code);
        BigDecimal rateBigDec = new BigDecimal(currency.getValue());
        BigDecimal amountBigDec = new BigDecimal(amount);
        return amountBigDec.multiply(rateBigDec, MathContext.DECIMAL32).toString();
    }

    @Override
    public String getCurrencyToCurrencyByCode(String amount, String code1, String code2) {
        if (returnAmount(amount, code1, code2)) return amount;
        String euro = getCurrencyToEuroByCode(amount, code1);
        return getEuroToCurrencyByCode(euro, code2);
    }

    private boolean returnAmount(String amount, String code1, String code2) {
        return code1.equalsIgnoreCase(code2);
    }

    private ArrayList<String> getDailyExchangeRates() {
        Map<Integer, JSONObject> jsonObjectHashMap = processKeysInCurrencyRateObject();
        ArrayList<String> arrOfRates = new ArrayList<>();
        for (int i = 0; i < jsonObjectHashMap.size(); i++) {
            arrOfRates.add(getCurrencyExchangeRate(jsonObjectHashMap, i));
        }
        return arrOfRates;
    }

    private ArrayList<String> getCurrencyCodes() {
        ArrayList<String> currencyCodes = new ArrayList<>();
        JSONArray values = getArrayOfCurrencyCodeObjects();
        for (int i = 0; i < values.length(); i++) {
            currencyCodes.add(getCurrencyCodeString(values, i));
        }
        return currencyCodes;
    }
}
