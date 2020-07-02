package com.currencyconverter.demo.services.implementations;

import com.currencyconverter.demo.exceptions.client.BadParameterException;
import com.currencyconverter.demo.exceptions.client.ResourceNotFoundException;
import com.currencyconverter.demo.exceptions.server.InternalServerErrorECB;
import com.currencyconverter.demo.helpers.ParameterValidityChecker;
import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.repository.contracts.CurrencyRepositoryECB;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static com.currencyconverter.demo.constants.ApiConstants.DATA_FOR_SINGLE_DAY;
import static com.currencyconverter.demo.constants.ApiConstants.INDEX_OF_FIRST_DAY;
import static com.currencyconverter.demo.constants.ControllerConstants.*;
import static com.currencyconverter.demo.constants.ControllerConstants.END_DATE;
import static com.currencyconverter.demo.helpers.DateFormatter.*;
import static com.currencyconverter.demo.helpers.PaginateFactory.paginateTimeSeries;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.*;
import static com.currencyconverter.demo.services.datanavigator.ecb.EcbDataProcessor.processKeysInCurrencyRateObject;
import static com.currencyconverter.demo.services.datanavigator.ecb.EcbJsonDataNavigator.*;


@Service
public class CurrencyServiceImpl implements CurrencyService {
    private CurrencyRepositoryECB currencyRepositoryECB;

    public CurrencyServiceImpl(CurrencyRepositoryECB currencyRepositoryECB) {
        this.currencyRepositoryECB = currencyRepositoryECB;
    }

    @Override
    public CurrencyCollection getAllCurrencies(String base) {
        JSONObject currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData();
        JSONArray values = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        String date = formatTodayDate();
        ArrayList[] codesArray = getCurrencyCodesAndNames(values);
        int indexOfBase = -1;
        if (!base.equalsIgnoreCase("eur")) {
            indexOfBase = getIndexOfBase(base, codesArray[0]);
        }
        List<Currency> listOfCurrencies = getListOfAllCurrencies(codesArray, currencyExchangeDataJson, DATA_FOR_SINGLE_DAY, DATA_FOR_SINGLE_DAY, base, indexOfBase, "all", values);
        return new CurrencyCollection(date, listOfCurrencies, base.toUpperCase());
    }

    @Override
    public CurrencyCollection getByCode(String codes, String base) {
        JSONObject currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData();
        int numberOfActiveDaysInTimeSeries = 1;
        List<CurrencyCollection> currencyCollectionList = getCurrencyCollectionForSpecificDates(LocalDate.now(), LocalDate.now(), base, codes, numberOfActiveDaysInTimeSeries, currencyExchangeDataJson);
        return currencyCollectionList.get(0);
    }

    @Override
    public Map<String, Map<String, String>> getAvailableCurrencies(boolean ever) {
        Map<String, String> currenciesMap = new TreeMap<>();
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        JSONObject currencyExchangeDataJson = checkIfEverIsTrue(ever);
        JSONArray values = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        ArrayList[] codes = getCurrencyCodesAndNames(values);
        for (int i = 0; i < codes[0].size(); i++) {
            currenciesMap.put(codes[0].get(i).toString(), codes[1].get(i).toString());
        }
        resultMap.put("currencies", currenciesMap);
        return resultMap;
    }

    @Override
    public CurrencyCollection getCurrenciesPerDate(LocalDate localDate, String codes, String base) {
        JSONObject currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData(localDate.toString());
        int numberOfActiveDaysInTimeSeries = 1;
        List<CurrencyCollection> currencyCollectionList = getCurrencyCollectionForSpecificDates(localDate, localDate, base, codes, numberOfActiveDaysInTimeSeries, currencyExchangeDataJson);
        return currencyCollectionList.get(0);
    }

    @Override
    public List<CurrencyCollection> getTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, String base, String codes) {
        int numberOfActiveDaysInTimeSeries = calculateActiveDaysOfTimeRange(localDateFrom, localDateTo).get("Active days");
        HashMap<String, LocalDate> mapOfPaginatedLocalDates = paginateTimeSeries(localDateFrom, localDateTo, page, limit);
        localDateFrom = mapOfPaginatedLocalDates.get(START_DATE);
        localDateTo = mapOfPaginatedLocalDates.get(END_DATE);
        JSONObject currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData(localDateFrom.toString(), localDateTo.toString());
        List<CurrencyCollection> currencyCollectionList = getCurrencyCollectionForSpecificDates(localDateFrom, localDateTo, base, codes, numberOfActiveDaysInTimeSeries, currencyExchangeDataJson);
        return currencyCollectionList;
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

    private List<CurrencyCollection> getCurrencyCollectionForSpecificDates(LocalDate localDateFrom, LocalDate localDateTo, String base, String codes, int numberOfActiveDaysInTimeSeries, JSONObject currencyExchangeDataJson) {
        HashMap<Integer, LocalDate> mapOfEasterDays = getMapOfEasterDays(localDateFrom, localDateTo);
        JSONArray values = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        ArrayList[] arrayOfAllCodes = getCurrencyCodesAndNames(values);
        List<CurrencyCollection> currencyCollectionList = new ArrayList<>();
        addCurrenciesToList(localDateFrom, localDateTo, base, codes, numberOfActiveDaysInTimeSeries, mapOfEasterDays, currencyExchangeDataJson, currencyCollectionList, values, arrayOfAllCodes);
        return currencyCollectionList;
    }

    private int getIndexOfBase(String base, ArrayList<String> code) {
        try {
            for (int i = 0; i < code.size(); i++) {
                if (code.get(i).equalsIgnoreCase(base)) {
                    return i;
                }
            }
        } catch (Exception e) {
            throw new InternalServerErrorECB("Base currency not found!");
        }
        return -1;
    }

    private JSONObject checkIfEverIsTrue(boolean ever) {
        JSONObject currencyExchangeDataJson;
        if (ever) {
            currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData(LocalDate.of(1999, 1, 1).toString(), LocalDate.now().toString());
        } else {
            currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData();
        }
        return currencyExchangeDataJson;
    }

    private void addCurrenciesToList(LocalDate localDateFrom, LocalDate localDateTo, String base, String codes, int numberOfActiveDaysInTimeSeries, HashMap<Integer, LocalDate> mapOfEasterDays, JSONObject currencyExchangeDataJson, List<CurrencyCollection> currencyCollectionList, JSONArray values, ArrayList[] arrayOfAllCodes) {
        int indexOfDay = INDEX_OF_FIRST_DAY;
        int indexOfBase = -1;
        if (!base.equalsIgnoreCase("eur")) {
            indexOfBase = getIndexOfBase(base, arrayOfAllCodes[0]);
        }
        while (localDateFrom.isBefore(localDateTo.plusDays(1))) {
            List<Currency> listOfCurrencies = getListOfAllCurrencies(arrayOfAllCodes, currencyExchangeDataJson, indexOfDay, numberOfActiveDaysInTimeSeries, base, indexOfBase, codes, values);
            currencyCollectionList.add(new CurrencyCollection(localDateFrom.toString(), listOfCurrencies, base.toUpperCase()));
            localDateFrom = localDateFrom.plusDays(1);
            indexOfDay = checkIfDayIndexShouldBeIncreased(localDateFrom, indexOfDay, mapOfEasterDays);
        }
    }

    private List<Currency> getListOfAllCurrencies(ArrayList[] allCodes, JSONObject currencyExchangeDataJson, int indexOfDay, int numberOfTimeSeriesDays, String base, int indexOfBase, String codes, JSONArray values) {
        List<Currency> codesAndRates = new ArrayList<>();
        ArrayList<String>[] codeIndexesOfSelectedCurrencies;
        if (!codes.equalsIgnoreCase("all")) {
            ArrayList<String> namesOfCurrencies = getListOfMultipleCurrencyCodes(codes);
            codeIndexesOfSelectedCurrencies = getCurrencyCodesAndNames(values, namesOfCurrencies);
        } else {
            codeIndexesOfSelectedCurrencies = allCodes;
        }
        ArrayList<ArrayList<String>> rates = getListOfDailyExchangeRates(currencyExchangeDataJson, numberOfTimeSeriesDays, allCodes[2], codes, values, indexOfBase, codeIndexesOfSelectedCurrencies);
        for (int i = 0; i < rates.size(); i++) {
            Currency currency = new Currency();
            currency.setValue(rates.get(i).get(indexOfDay));
            currency.setCode(codeIndexesOfSelectedCurrencies[0].get(i));
            currency.setCurrencyName(codeIndexesOfSelectedCurrencies[1].get(i));
            codesAndRates.add(currency);
        }
        return codesAndRates;
    }

    private static ArrayList[] getCurrencyCodesAndNames(JSONArray arrayOfCurrencyCodeObjects) {
        ArrayList<String> currencyCodes = new ArrayList<>();
        ArrayList<String> currencyNames = new ArrayList<>();
        ArrayList<String> currencyIndex = new ArrayList<>();
        for (int i = 0; i < arrayOfCurrencyCodeObjects.length(); i++) {
            currencyCodes.add(getCurrencyCodeString(arrayOfCurrencyCodeObjects, i));
            currencyNames.add(getCurrencyNameString(arrayOfCurrencyCodeObjects, i));
            currencyIndex.add(String.valueOf(i));
        }
        return new ArrayList[]{currencyCodes, currencyNames, currencyIndex};
    }

    private static ArrayList<String>[] getCurrencyCodesAndNames(JSONArray arrayOfCurrencyCodeObjects, ArrayList<String> namesOfCurrencies) {
        ArrayList<String> currencyCodes = new ArrayList<>();
        ArrayList<String> currencyNames = new ArrayList<>();
        ArrayList<String> currencyIndex = new ArrayList<>();
        for (int i = 0; i < arrayOfCurrencyCodeObjects.length(); i++) {
            String codeInJsonArray = getCurrencyCodeString(arrayOfCurrencyCodeObjects, i);
            if (namesOfCurrencies.contains(codeInJsonArray)) {
                currencyCodes.add(getCurrencyCodeString(arrayOfCurrencyCodeObjects, i));
                currencyNames.add(getCurrencyNameString(arrayOfCurrencyCodeObjects, i));
                currencyIndex.add(String.valueOf(i));
            }
        }
        return new ArrayList[]{currencyCodes, currencyNames, currencyIndex};
    }

    private static ArrayList<ArrayList<String>> getListOfDailyExchangeRates(JSONObject currencyExchangeDataJson, int numberOfTimeSeriesDays, ArrayList<String> codesIndexesArray, String codes, JSONArray values, int indexOfBase, ArrayList<String>[] codesOfSelectedCurrencies) {
        Map<Integer, JSONObject> jsonObjectHashMap = processKeysInCurrencyRateObject(currencyExchangeDataJson, codesIndexesArray);
        ArrayList<String> codeIndexesOfSelectedCurrencies = codesOfSelectedCurrencies[2];
        ArrayList<String> arrayOfBases = new ArrayList<>();
        ArrayList<ArrayList<String>> arrOfRates = new ArrayList<>();
        for (String s : codeIndexesOfSelectedCurrencies) {
            arrOfRates.add(getCurrencyExchangeRate(jsonObjectHashMap, Integer.parseInt(s), numberOfTimeSeriesDays));
        }
        if (indexOfBase != -1) {
            arrayOfBases = getListOfDailyBases(indexOfBase, jsonObjectHashMap, numberOfTimeSeriesDays);
            for (int i = 0; i < arrOfRates.size(); i++) {
                calculateRate(arrayOfBases, arrOfRates, i);
            }
        }
        return arrOfRates;
    }

    private static void calculateRate(ArrayList<String> arrayOfBases, ArrayList<ArrayList<String>> arrOfRates, int i) {
        for (int j = 0; j < arrOfRates.get(i).size(); j++) {
            try {
                String base = arrayOfBases.get(j);
                double baseValueDouble = Double.parseDouble(base);
                double result = Double.parseDouble(arrOfRates.get(i).get(j)) / baseValueDouble;
                BigDecimal bd = BigDecimal.valueOf(result);
                bd = bd.setScale(4, RoundingMode.HALF_UP);
                arrOfRates.get(i).set(j, bd.toString());
            } catch (Exception e) {
                arrOfRates.get(i).set(j, "Service unavailable");
            }
        }
    }

    private static ArrayList<String> getListOfDailyBases(int indexOfBase, Map<Integer, JSONObject> jsonObjectHashMap, int numberOfTimeSeriesDays) {
        return getCurrencyExchangeRate(jsonObjectHashMap, indexOfBase, numberOfTimeSeriesDays);
    }

    private static ArrayList<String> getListOfMultipleCurrencyCodes(String codes) {
        String[] arrayOfCodes = codes.split(",");
        ArrayList<String> listOfCurrencyCodes = new ArrayList<>();
        for (String code : arrayOfCodes) {
            checkIfCodePathVariableIsCorrect(code);
            listOfCurrencyCodes.add(code.toUpperCase());
        }
        return listOfCurrencyCodes;
    }
}
