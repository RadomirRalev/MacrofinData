package com.currencyconverter.demo.services.implementations;

import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.repository.contracts.CurrencyRepositoryECB;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.currencyconverter.demo.constants.ApiConstants.DATA_FOR_SINGLE_DAY;
import static com.currencyconverter.demo.helpers.DateFormatter.formatTodayDate;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.checkDateParameter;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.checkIfFromDateIsBeforeToDate;
import static com.currencyconverter.demo.repository.repositoryhelpers.ecb.EcbJsonDataNavigator.getArrayOfCurrencyCodeObjects;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepositoryECB currencyRepositoryECB;

    public CurrencyServiceImpl(CurrencyRepositoryECB currencyRepositoryECB) {
        this.currencyRepositoryECB = currencyRepositoryECB;
    }

    @Override
    public CurrencyCollection getAllCurrencies() {
        return currencyRepositoryECB.getAllCurrencies();
    }

    @Override
    public CurrencyCollection getByCode(String codes) {
        return currencyRepositoryECB.getByCode(codes);
    }

    @Override
    public Map<String, Map<String, String>> getAvailableCurrencies() {
        return currencyRepositoryECB.getAvailableCurrencies();
    }

    @Override
    public CurrencyCollection getCurrenciesPerDate(String date) {
        LocalDate localDate = checkDateParameter(date);
        return currencyRepositoryECB.getCurrenciesPerDate(localDate);
    }

    @Override
    public List<CurrencyCollection> getTimeSeries(String from, String to,  String page, String limit) {
        LocalDate localDateFrom = checkDateParameter(from);
        LocalDate localDateTo = checkDateParameter(to);
        checkIfFromDateIsBeforeToDate(localDateFrom, localDateTo);
        return currencyRepositoryECB.getTimeSeries(localDateFrom, localDateTo, page, limit);
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

}
