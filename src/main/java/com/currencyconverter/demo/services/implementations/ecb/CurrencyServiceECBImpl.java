package com.currencyconverter.demo.services.implementations.ecb;

import com.currencyconverter.demo.exceptions.server.InternalServerErrorECB;
import com.currencyconverter.demo.helpers.MultiThreadFactory;
import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.repository.contracts.ecb.CurrencyRepositoryECB;
import com.currencyconverter.demo.services.contracts.ecb.CurrencyServiceECB;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

import static com.currencyconverter.demo.constants.ApiConstants.*;
import static com.currencyconverter.demo.constants.ControllerConstants.*;
import static com.currencyconverter.demo.constants.ControllerConstants.END_DATE;
import static com.currencyconverter.demo.helpers.DateFormatter.*;
import static com.currencyconverter.demo.helpers.MoneyFormatter.formatSum;
import static com.currencyconverter.demo.helpers.PaginateFactory.paginateTimeSeries;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.*;
import static com.currencyconverter.demo.services.implementations.ecb.EcbDataProcessor.processKeysInCurrencyRateObject;
import static com.currencyconverter.demo.services.implementations.ecb.EcbJsonDataNavigator.*;


@Service
public class CurrencyServiceECBImpl implements CurrencyServiceECB {
    private CurrencyRepositoryECB currencyRepositoryECB;

    public CurrencyServiceECBImpl(CurrencyRepositoryECB currencyRepositoryECB) {
        this.currencyRepositoryECB = currencyRepositoryECB;
    }

    @Override
    public ArrayList<Currency> getCurrenciesPerSingleDay(LocalDate localDate, ArrayList<String> listOfCurrencyCodes, String base) {
        JSONObject currencyExchangeDataJson = getCurrencyExchangeData(localDate);
        ArrayList<ArrayList<Currency>> currencyCollectionList = getCurrencyCollectionForSpecificDates(localDate, localDate, base, listOfCurrencyCodes, TIMESERIES_ONE_DAY, currencyExchangeDataJson);
        return currencyCollectionList.get(FIRST_DAY_OF_CURRENCY_COLLECTION);
    }

    @Override
    public Map<String, Map<String, String>> getAvailableCurrencies(boolean ever) {
        Map<String, String> currenciesMap = new TreeMap<>();
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        JSONObject currencyExchangeDataJson = getCurrencyExchangeData(ever);
        JSONArray values = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        ArrayList[] codes = getCurrencyCodesAndNames(values);
        for (int i = 0; i < codes[0].size(); i++) {
            currenciesMap.put(codes[0].get(i).toString(), codes[1].get(i).toString());
        }
        resultMap.put("currencies", currenciesMap);
        return resultMap;
    }

    @Override
    public ArrayList<ArrayList<Currency>> getTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, String base, ArrayList<String> listOfCurrencyCodes) {
        int numberOfActiveDaysInTimeSeries = calculateActiveDaysOfTimeRange(localDateFrom, localDateTo).get("Active days");
        HashMap<String, LocalDate> mapOfPaginatedLocalDates = paginateTimeSeries(localDateFrom, localDateTo, page, limit);
        localDateFrom = mapOfPaginatedLocalDates.get(START_DATE);
        localDateTo = mapOfPaginatedLocalDates.get(END_DATE);
        JSONObject currencyExchangeDataJson = getCurrencyExchangeData(localDateFrom, localDateTo);
        return getCurrencyCollectionForSpecificDates(localDateFrom, localDateTo, base, listOfCurrencyCodes, numberOfActiveDaysInTimeSeries, currencyExchangeDataJson);
    }

    private static ArrayList<ArrayList<Currency>> getCurrencyCollectionForSpecificDates(LocalDate localDateFrom, LocalDate localDateTo, String base, ArrayList<String> listOfCurrencyCodes, int numberOfActiveDaysInTimeSeries, JSONObject currencyExchangeDataJson) {
        HashMap<Integer, LocalDate> mapOfEasterDays = getMapOfEasterDays(localDateFrom, localDateTo);
        JSONArray values = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        ArrayList[] arrayOfAllCodes = getCurrencyCodesAndNames(values);
        ArrayList<ArrayList<Currency>> currenciesList = new ArrayList<>();
        addCurrenciesToList(localDateFrom, localDateTo, base, listOfCurrencyCodes, numberOfActiveDaysInTimeSeries, mapOfEasterDays, currencyExchangeDataJson, currenciesList, values, arrayOfAllCodes);
        return currenciesList;
    }

    private static int getIndexOfBase(String base, ArrayList<String> code) {
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

    private JSONObject getCurrencyExchangeData(boolean ever) {
        JSONObject currencyExchangeDataJson;
        try {
            if (ever) {
                currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData(LocalDate.of(1999, 1, 1).toString(), LocalDate.now().toString());
            } else {
                currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData();
            }
        } catch (Exception e) {
            throw new InternalServerErrorECB("Cannot process 'ever' attribute!");
        }
        return currencyExchangeDataJson;
    }

    private JSONObject getCurrencyExchangeData(LocalDate localDate) {
        JSONObject currencyExchangeDataJson;
        try {
            if (localDate.equals(LocalDate.now())) {
                currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData();
            } else {
                currencyExchangeDataJson = currencyRepositoryECB.getCurrencyExchangeData(localDate.toString());
            }
        } catch (Exception e) {
            throw new InternalServerErrorECB("Cannot process date!");
        }
        return currencyExchangeDataJson;
    }

    private JSONObject getCurrencyExchangeData(LocalDate localDateFrom, LocalDate localDateTo) {
        JSONObject currencyExchangeDataJson;
        try {
            currencyExchangeDataJson =  currencyRepositoryECB.getCurrencyExchangeData(localDateFrom.toString(), localDateTo.toString());
        } catch (Exception e) {
            throw new InternalServerErrorECB("Cannot process date!");
        }
        return currencyExchangeDataJson;
    }

    private static void addCurrenciesToList(LocalDate localDateFrom, LocalDate localDateTo, String base, ArrayList<String> listOfCurrencyCodes, int numberOfActiveDaysInTimeSeries, HashMap<Integer, LocalDate> mapOfEasterDays, JSONObject currencyExchangeDataJson, ArrayList<ArrayList<Currency>> currenciesList, JSONArray values, ArrayList[] arrayOfAllCodes) {
        int indexOfDay = INDEX_OF_FIRST_DAY;
        int indexOfBase = -1;
        indexOfBase = checkIfBaseIsEur(base, arrayOfAllCodes, indexOfBase);
        while (localDateFrom.isBefore(localDateTo.plusDays(1))) {
            try {
                currenciesList.add(getListOfAllCurrencies(arrayOfAllCodes, currencyExchangeDataJson, indexOfDay, numberOfActiveDaysInTimeSeries, indexOfBase, listOfCurrencyCodes, values));
                localDateFrom = localDateFrom.plusDays(1);
                indexOfDay = checkIfDayIndexShouldBeIncreased(localDateFrom, indexOfDay, mapOfEasterDays);
            } catch (Exception e) {
                throw new InternalServerErrorECB("Cannot add ECB currencies to list!");
            }
        }
    }

    private static int checkIfBaseIsEur(String base, ArrayList[] arrayOfAllCodes, int indexOfBase) {
        try {
            if (!base.equalsIgnoreCase("eur")) {
                indexOfBase = getIndexOfBase(base, arrayOfAllCodes[0]);
            }
        } catch (Exception e) {
            throw new InternalServerErrorECB("Cannot check if base currency is euro!");
        }
        return indexOfBase;
    }

    private static ArrayList<Currency> getListOfAllCurrencies(ArrayList[] allCodes, JSONObject currencyExchangeDataJson, int indexOfDay, int numberOfTimeSeriesDays, int indexOfBase, ArrayList<String> listOfCurrencyCodes, JSONArray values) {
        ArrayList<Currency> codesAndRates = new ArrayList<>();
        ArrayList<String>[] codeIndexesOfSelectedCurrencies;
        codeIndexesOfSelectedCurrencies = checkIfAllOrSpecificCodes(allCodes, listOfCurrencyCodes, values);
        ArrayList<ArrayList<String>> rates = getListOfDailyExchangeRates(currencyExchangeDataJson, numberOfTimeSeriesDays, allCodes[2], indexOfBase, codeIndexesOfSelectedCurrencies);
            for (int i = 0; i < rates.size(); i++) {
            Currency currency = new Currency();
            try {
                currency.setValue(rates.get(i).get(indexOfDay));
                currency.setCode(codeIndexesOfSelectedCurrencies[0].get(i));
                currency.setCurrencyName(codeIndexesOfSelectedCurrencies[1].get(i));
            } catch (Exception e) {
                throw new InternalServerErrorECB("Cannot create currency!");
            }
            codesAndRates.add(currency);
        }
        return codesAndRates;
    }

    private static ArrayList<String>[] checkIfAllOrSpecificCodes(ArrayList[] allCodes, ArrayList<String> listOfCurrencyCodes, JSONArray values) {
        ArrayList<String>[] codeIndexesOfSelectedCurrencies;
        try {
            if (!listOfCurrencyCodes.get(0).equalsIgnoreCase("all")) {
                codeIndexesOfSelectedCurrencies = getCurrencyCodesAndNames(values, listOfCurrencyCodes);
            } else {
                codeIndexesOfSelectedCurrencies = allCodes;
            }
        } catch (Exception e) {
            throw new InternalServerErrorECB("Cannot check if all or specific ECB currencies should be returned!");
        }
        return codeIndexesOfSelectedCurrencies;
    }

    private static ArrayList[] getCurrencyCodesAndNames(JSONArray arrayOfCurrencyCodeObjects) {
        ArrayList<String> currencyCodes = new ArrayList<>();
        ArrayList<String> currencyNames = new ArrayList<>();
        ArrayList<String> currencyIndex = new ArrayList<>();
        for (int i = 0; i < arrayOfCurrencyCodeObjects.length(); i++) {
            try {
                currencyCodes.add(getCurrencyCodeString(arrayOfCurrencyCodeObjects, i));
                currencyNames.add(getCurrencyNameString(arrayOfCurrencyCodeObjects, i));
                currencyIndex.add(String.valueOf(i));
            } catch (Exception e) {
                throw new InternalServerErrorECB("Cannot add ECB currency codes and/or names!");
            }
        }
        return new ArrayList[]{currencyCodes, currencyNames, currencyIndex};
    }

    private static ArrayList<String>[] getCurrencyCodesAndNames(JSONArray arrayOfCurrencyCodeObjects, ArrayList<String> namesOfCurrencies) {
        ArrayList<String> currencyCodes = new ArrayList<>();
        ArrayList<String> currencyNames = new ArrayList<>();
        ArrayList<String> currencyIndex = new ArrayList<>();
        for (int i = 0; i < arrayOfCurrencyCodeObjects.length(); i++) {
            try {
                String codeInJsonArray = getCurrencyCodeString(arrayOfCurrencyCodeObjects, i);
                if (namesOfCurrencies.contains(codeInJsonArray)) {
                    currencyCodes.add(getCurrencyCodeString(arrayOfCurrencyCodeObjects, i));
                    currencyNames.add(getCurrencyNameString(arrayOfCurrencyCodeObjects, i));
                    currencyIndex.add(String.valueOf(i));
                }
            } catch (Exception e) {
                throw new InternalServerErrorECB("Cannot get ECB currency codes and/or names!");
            }
        }
        return new ArrayList[]{currencyCodes, currencyNames, currencyIndex};
    }

    private static ArrayList<ArrayList<String>> getListOfDailyExchangeRates(JSONObject currencyExchangeDataJson, int numberOfTimeSeriesDays, ArrayList<String> codesIndexesArray, int indexOfBase, ArrayList<String>[] codesOfSelectedCurrencies) {
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
            multiplyByBase(arrayOfBases, arrOfRates, i, j);
        }
    }

    private static void multiplyByBase(ArrayList<String> arrayOfBases, ArrayList<ArrayList<String>> arrOfRates, int i, int j) {
        try {
            String base = arrayOfBases.get(j);
            double baseValueDouble = Double.parseDouble(base);
            double result = Double.parseDouble(arrOfRates.get(i).get(j)) / baseValueDouble;
            BigDecimal bd = formatSum(result);
            arrOfRates.get(i).set(j, bd.toString());
        } catch (Exception e) {
            arrOfRates.get(i).set(j, "Service unavailable");
        }
    }

    private static ArrayList<String> getListOfDailyBases(int indexOfBase, Map<Integer, JSONObject> jsonObjectHashMap, int numberOfTimeSeriesDays) {
        return getCurrencyExchangeRate(jsonObjectHashMap, indexOfBase, numberOfTimeSeriesDays);
    }
}
