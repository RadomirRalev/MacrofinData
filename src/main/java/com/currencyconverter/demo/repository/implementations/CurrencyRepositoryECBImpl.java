package com.currencyconverter.demo.repository.implementations;

import com.currencyconverter.demo.exceptions.client.BadParameterException;
import com.currencyconverter.demo.exceptions.client.ResourceNotFoundException;
import com.currencyconverter.demo.exceptions.server.BadGatewayException;
import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.repository.contracts.CurrencyRepositoryECB;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.currencyconverter.demo.constants.ApiConstants.DATA_FOR_SINGLE_DAY;
import static com.currencyconverter.demo.constants.ApiConstants.INDEX_OF_FIRST_DAY;
import static com.currencyconverter.demo.constants.ControllerConstants.*;
import static com.currencyconverter.demo.constants.ExceptionConstants.*;
import static com.currencyconverter.demo.helpers.DateFormatter.*;
import static com.currencyconverter.demo.repository.repositoryhelpers.ecb.EcbQueryBuilder.getCurrencyRatesURI;
import static com.currencyconverter.demo.repository.repositoryhelpers.ecb.EcbDataProcessor.processKeysInCurrencyRateObject;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.checkIfCodePathVariableIsCorrect;
import static com.currencyconverter.demo.repository.repositoryhelpers.ecb.EcbJsonDataNavigator.*;

@Repository
public class CurrencyRepositoryECBImpl implements CurrencyRepositoryECB {

    @Override
    public JSONObject getCurrencyExchangeData() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return new JSONObject(restTemplate.getForObject(getCurrencyRatesURI(), String.class));
        } catch (Exception e) {
            throw new BadGatewayException(NO_CONNECTION_WITH_ECB_SERVER);
        }
    }

    public JSONObject getCurrencyExchangeData(String date) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return new JSONObject(restTemplate.getForObject(getCurrencyRatesURI(date), String.class));
        } catch (Exception e) {
            throw new BadGatewayException(NO_CONNECTION_WITH_ECB_SERVER);
        }
    }

    public JSONObject getCurrencyExchangeData(String from, String to) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return new JSONObject(restTemplate.getForObject(getCurrencyRatesURI(from, to), String.class));
        } catch (Exception e) {
            throw new BadGatewayException(NO_CONNECTION_WITH_ECB_SERVER);
        }
    }

    @Override
    public CurrencyCollection getAllCurrencies() {
        JSONObject currencyExchangeDataJson = getCurrencyExchangeData();
        JSONArray arrayOfCurrencyCodeObjects = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        String date = formatTodayDate();
        return new CurrencyCollection(date, getListOfAllCurrencies(arrayOfCurrencyCodeObjects, currencyExchangeDataJson, DATA_FOR_SINGLE_DAY, DATA_FOR_SINGLE_DAY));
    }

    @Override
    public CurrencyCollection getByCode(String codes) {
        JSONObject currencyExchangeDataJson = getCurrencyExchangeData();
        JSONArray values = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        List<Currency> listOfCurrencies = getListOfMultipleCurrenciesByCode(codes, values, currencyExchangeDataJson, DATA_FOR_SINGLE_DAY);
        return new CurrencyCollection(formatTodayDate(), listOfCurrencies);
    }

    @Override
    public Map<String, Map<String, String>> getAvailableCurrencies() {
        Map<String, String> currenciesMap = new TreeMap<>();
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        JSONObject currencyExchangeDataJson = getCurrencyExchangeData();
        JSONArray arrayOfCurrencyCodeObjects = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        List<Currency> allCurrencies = getListOfAllCurrencies(arrayOfCurrencyCodeObjects, currencyExchangeDataJson, DATA_FOR_SINGLE_DAY, DATA_FOR_SINGLE_DAY);
        for (Currency currency : allCurrencies) {
            currenciesMap.put(currency.getCode(), currency.getCurrencyName());
        }
        resultMap.put("currencies", currenciesMap);
        return resultMap;
    }

    @Override
    public CurrencyCollection getCurrenciesPerDate(LocalDate localDate) {
        JSONObject currencyExchangeDataJson = getCurrencyExchangeData(localDate.toString());
        JSONArray arrayOfCurrencyCodeObjects = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        return new CurrencyCollection(localDate.toString(), getListOfAllCurrencies(arrayOfCurrencyCodeObjects, currencyExchangeDataJson, DATA_FOR_SINGLE_DAY, DATA_FOR_SINGLE_DAY));
    }

    @Override
    public List<CurrencyCollection> getTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit) {
        int numberOfActiveDaysInTimeSeries = calculateActiveDaysOfTimeRange(localDateFrom, localDateTo).get("Active days");
        HashMap<String, LocalDate> mapOfPaginatedLocalDates = paginateTimeSeries(localDateFrom, localDateTo, page, limit);
        localDateFrom = mapOfPaginatedLocalDates.get("Start date");
        localDateTo = mapOfPaginatedLocalDates.get("End date");
        HashMap<Integer, LocalDate> mapOfEasterDays = getMapOfEasterDays(localDateFrom, localDateTo);
        JSONObject currencyExchangeDataJson = getCurrencyExchangeData(localDateFrom.toString(), localDateTo.toString());
        JSONArray arrayOfCurrencyCodeObjects = getArrayOfCurrencyCodeObjects(currencyExchangeDataJson);
        List<CurrencyCollection> currencyCollectionList = new ArrayList<>();
        int indexOfDay = INDEX_OF_FIRST_DAY;
        while (localDateFrom.isBefore(localDateTo.plusDays(1))) {
            currencyCollectionList.add(new CurrencyCollection(localDateFrom.toString(), getListOfAllCurrencies(arrayOfCurrencyCodeObjects, currencyExchangeDataJson, indexOfDay, numberOfActiveDaysInTimeSeries)));
            localDateFrom = localDateFrom.plusDays(1);
            indexOfDay = checkIfDayIndexShouldBeIncreased(localDateFrom, indexOfDay, mapOfEasterDays);
        }
        return currencyCollectionList;
    }

    private HashMap<String, LocalDate> paginateTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit) {
        HashMap<String, LocalDate> mapOfLocalDatesPaginated = new HashMap<>();
        if (page.equalsIgnoreCase(LAST_PAGE)) {
            page = getLastPage(localDateFrom, localDateTo, limit);
        }
        localDateFrom = getLocalDatePaginated(localDateFrom, localDateTo, page, limit);
        mapOfLocalDatesPaginated.put(START_DATE, localDateFrom);
        LocalDate localDateToPaginated = localDateFrom.plusDays(Integer.parseInt(limit) - 1);
        if (localDateToPaginated.isAfter(localDateTo)) {
            mapOfLocalDatesPaginated.put(END_DATE, localDateTo);
        } else {
            mapOfLocalDatesPaginated.put(END_DATE, localDateToPaginated);
        }
        return mapOfLocalDatesPaginated;
    }

    private String getLastPage(LocalDate localDateFrom, LocalDate localDateTo, String limit) {
        String page;
        int totalNumberOfDaysInTimeSeries = calculateActiveDaysOfTimeRange(localDateFrom, localDateTo).get("Total days");
        int pageInt = (totalNumberOfDaysInTimeSeries + Integer.parseInt(limit) - 1) / Integer.parseInt(limit);
        page = Integer.toString(pageInt);
        return page;
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

    private LocalDate getLocalDatePaginated(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit) {
        LocalDate localDateFromPaginated = localDateFrom.plusDays((Integer.parseInt(page) - 1) * Integer.parseInt(limit));
        if (localDateFromPaginated.isAfter(localDateTo)) {
            throw new BadParameterException(BAD_PARAMETER_PAGE_NUMBER_HIGHER_THAN_SELECTION);
        } else {
            localDateFrom = localDateFromPaginated;
        }
        return localDateFrom;
    }

    private HashMap<Integer, LocalDate> getMapOfEasterDays(LocalDate localDateFrom, LocalDate localDateTo) {
        HashMap<Integer, LocalDate> mapOfEasterDays = new HashMap<>();
        for (int i = localDateFrom.getYear(); i <= localDateTo.getYear(); i++) {
            mapOfEasterDays.put(i, calculateEaster(i));
        }
        return mapOfEasterDays;
    }

    private int checkIfDayIndexShouldBeIncreased(LocalDate localDateFrom, int indexOfDay, HashMap<Integer, LocalDate> mapOfEasterDays) {
        if (!(localDateFrom.getDayOfWeek().equals(DayOfWeek.SUNDAY) || localDateFrom.getDayOfWeek().equals(DayOfWeek.SATURDAY))
                && !(localDateFrom.getMonthValue() == 1 && localDateFrom.getDayOfMonth() == 1)
                && !(localDateFrom.getMonthValue() == 5 && localDateFrom.getDayOfMonth() == 1)
                && !(localDateFrom.getMonthValue() == 12 && localDateFrom.getDayOfMonth() == 25)
                && !(localDateFrom.getMonthValue() == 12 && localDateFrom.getDayOfMonth() == 26)
                && !(localDateFrom.getMonthValue() == 4)
                && !(localDateFrom.getMonthValue() == 3)) {
            indexOfDay++;
        }
        if (localDateFrom.getMonthValue() == 3 || localDateFrom.getMonthValue() == 4) {
            if (!(localDateFrom.getDayOfWeek().equals(DayOfWeek.SUNDAY) || localDateFrom.getDayOfWeek().equals(DayOfWeek.SATURDAY))
                    && !(localDateFrom.equals(mapOfEasterDays.get(localDateFrom.getYear()).plus(1, ChronoUnit.DAYS)))
                    && !(localDateFrom.equals(mapOfEasterDays.get(localDateFrom.getYear()).minus(2, ChronoUnit.DAYS)))) {
                indexOfDay++;
            }
        }
        return indexOfDay;
    }

    private List<Currency> getListOfMultipleCurrenciesByCode(String codes, JSONArray values, JSONObject currencyExchangeDataJson, int numberOfTimeSeriesDays) {
        String[] arrayOfCodes = codes.split(",");
        List<Currency> listOfCurrencies = new ArrayList<>();
        for (String code : arrayOfCodes) {
            checkIfCodePathVariableIsCorrect(code);
            listOfCurrencies.add(getCurrencyRestObject(code, values, currencyExchangeDataJson, numberOfTimeSeriesDays));
        }
        return listOfCurrencies;
    }

    private static Currency getCurrencyRestObject(String code, JSONArray values, JSONObject currencyExchangeDataJson, int numberOfTimeSeriesDays) {
        Currency currency1 = new Currency();
        if (getEuroIfCodeIsEur(code, currency1)) {
            return currency1;
        }
        int index1 = getIndexOfSingleCurrency(code, values);
        currency1.setCode(code.toUpperCase());
        currency1.setCurrencyName(getCurrencyNameString(values, index1));
        currency1.setValue(getSingleDailyExchangeRate(index1, currencyExchangeDataJson, numberOfTimeSeriesDays).get(0));
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

    private static List<String> getSingleDailyExchangeRate(int index, JSONObject currencyExchangeDataJson, int numberOfTimeSeriesDays) {
        Map<Integer, JSONObject> jsonObjectHashMap = processKeysInCurrencyRateObject(currencyExchangeDataJson);
        return getCurrencyExchangeRate(jsonObjectHashMap, index, numberOfTimeSeriesDays);
    }

    private static ArrayList<ArrayList<String>> getListOfDailyExchangeRates(JSONObject currencyExchangeDataJson, int numberOfTimeSeriesDays) {
        Map<Integer, JSONObject> jsonObjectHashMap = processKeysInCurrencyRateObject(currencyExchangeDataJson);
        ArrayList<ArrayList<String>> arrOfRates = new ArrayList<>();
        for (int i = 0; i < jsonObjectHashMap.size(); i++) {
            arrOfRates.add(getCurrencyExchangeRate(jsonObjectHashMap, i, numberOfTimeSeriesDays));
        }
        return arrOfRates;
    }


    private static ArrayList[] getCurrencyCodesAndNames(JSONArray arrayOfCurrencyCodeObjects) {
        ArrayList<String> currencyCodes = new ArrayList<>();
        ArrayList<String> currencyNames = new ArrayList<>();
        for (int i = 0; i < arrayOfCurrencyCodeObjects.length(); i++) {
            currencyCodes.add(getCurrencyCodeString(arrayOfCurrencyCodeObjects, i));
            currencyNames.add(getCurrencyNameString(arrayOfCurrencyCodeObjects, i));
        }
        return new ArrayList[]{currencyCodes, currencyNames};
    }

    private static List<Currency> getListOfAllCurrencies(JSONArray arrayOfCurrencyCodeObjects, JSONObject currencyExchangeDataJson, int indexOfDay, int numberOfTimeSeriesDays) {
        List<Currency> codesAndRates = new ArrayList<>();
        ArrayList[] codes = getCurrencyCodesAndNames(arrayOfCurrencyCodeObjects);
        ArrayList<ArrayList<String>> rates = getListOfDailyExchangeRates(currencyExchangeDataJson, numberOfTimeSeriesDays);
        for (int i = 0; i < rates.size(); i++) {
            Currency currency = new Currency();
            currency.setCode(codes[0].get(i).toString());
            currency.setValue(rates.get(i).get(indexOfDay));
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

