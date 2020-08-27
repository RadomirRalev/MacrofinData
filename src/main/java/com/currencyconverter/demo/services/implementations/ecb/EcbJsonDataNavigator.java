package com.currencyconverter.demo.services.implementations.ecb;

import com.currencyconverter.demo.exceptions.server.InternalServerErrorECB;
import com.currencyconverter.demo.helpers.ParameterValidityChecker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.currencyconverter.demo.constants.ApiConstants.*;
import static com.currencyconverter.demo.constants.ExceptionConstants.*;

public class EcbJsonDataNavigator {
    private static Logger logger = LoggerFactory.getLogger(ParameterValidityChecker.class);

    static JSONObject getObjectContainingNestedRateObjects(JSONObject currencyExchangeDataJson) {
        JSONArray dataSets = getJsonArrayFromJsonObject(currencyExchangeDataJson, KEY_OF_CURRENCY_DATA_ARRAY);
        JSONObject objectContainingExchangeRateSeries = getJsonObjectFromJsonArray(dataSets, INDEX_OF_OBJECT_CONTAINING_EXCHANGE_RATE_SERIES);
        return getNestedJsonObject(objectContainingExchangeRateSeries, NAME_OF_OBJECT_CONTAINING_NESTED_EXCHANGE_RATE_OBJECTS);
    }

    static JSONArray getArrayOfCurrencyCodeObjects(JSONObject currencyExchangeDateJson) {
        JSONObject structure = getNestedJsonObject(currencyExchangeDateJson, NAME_OF_OBJECT_CONTAINING_DESCRIPTION_OF_DATA);
        JSONObject attributes = getNestedJsonObject(structure, NAME_OF_OBJECT_CONTAINING_ATTRIBUTES_OF_DATA);
        JSONArray series = getJsonArrayFromJsonObject(attributes, KEY_OF_CURRENCY_CODES_ARRAY);
        JSONObject objectInsideSeriesArray = getJsonObjectFromJsonArray(series, INDEX_OF_OBJECT_CONTAINING_CURRENCY_CODES_ARRAY);
        return getJsonArrayFromJsonObject(objectInsideSeriesArray, KEY_OF_ARRAY_CONTAINING_THREE_LETTER_CURRENCY_CODE);
    }

    static String getCurrencyCodeString(JSONArray values, int i) {
        if (values.isEmpty() || values.isNull(i)) {
            if (values.isNull(i)) {
                logger.error(BAD_REQUEST_IN_JSON_FILE_NO_VALUES + i);
            } else {
                logger.error(BAD_REQUEST_JSON_ARRAY_IS_EMPTY);
            }
            throw new InternalServerErrorECB(UNVALID_RESPONSE_ECB_SERVER);
        }
        try {
            JSONObject jsonObjArr = new JSONObject(values.getJSONObject(i).toString());
            return jsonObjArr.getString(KEY_OF_CURRENCY_CODE_IN_OBJECT);
        } catch (Exception e) {
            logger.error(BAD_REQUEST_IN_JSON_FILE_WRONG_KEY + " - " + KEY_OF_CURRENCY_CODE_IN_OBJECT);
            throw new InternalServerErrorECB(UNVALID_RESPONSE_ECB_SERVER);
        }
    }

    static String getCurrencyNameString(JSONArray values, int i) {
        try {
            JSONObject jsonObjArr = new JSONObject(values.getJSONObject(i).toString());
            return jsonObjArr.getString(KEY_OF_CURRENCY_NAME_IN_OBJECT);
        } catch (Exception e) {
            logger.error(BAD_REQUEST_IN_JSON_FILE_WRONG_KEY + " - " + KEY_OF_CURRENCY_CODE_IN_OBJECT);
            throw new InternalServerErrorECB(UNVALID_RESPONSE_ECB_SERVER);
        }
    }

    static ArrayList<String> getCurrencyExchangeRate(Map<Integer, JSONObject> jsonObjectHashMap, int i, int numberOfDaysInTimeSeries) {
        JSONObject objectContainingArrayOfRates = getObjectContainingArrayOfRates(jsonObjectHashMap, i);
        Integer key = getKeyOfExchangeRatesArray(jsonObjectHashMap, i);
        int largestKey = getLargestKey(jsonObjectHashMap, i);
        ArrayList<String> resultList = new ArrayList<>();
        int startingIndex = getStartingIndex(key, largestKey);
        addResultToList(numberOfDaysInTimeSeries, objectContainingArrayOfRates, largestKey, resultList, startingIndex);
        return resultList;
    }

    private static void addResultToList(int numberOfDaysInTimeSeries, JSONObject objectContainingArrayOfRates, int largestKey, ArrayList<String> resultList, int startingIndex) {
        for (int j = 0; j < numberOfDaysInTimeSeries; j++) {
            Integer keyOfRate = j;
            if (j < startingIndex || j > largestKey) {
                resultList.add("Service unavailable");
            } else {
                try {
                    JSONArray arr = objectContainingArrayOfRates.getJSONArray(keyOfRate.toString());
                    Double rate = arr.getDouble(0);
                    BigDecimal bd = BigDecimal.valueOf(rate);
                    bd = bd.setScale(4, RoundingMode.HALF_UP);
                    resultList.add(bd.toString());

                } catch (Exception e) {
                    resultList.add("Service unavailable");
                }
            }
        }
    }

    private static int getStartingIndex(Integer key, int largestKey) {
        return (largestKey - key) + 1;
    }

    private static Integer getKeyOfExchangeRatesArray(Map<Integer, JSONObject> jsonObjectHashMap, int i) {
        try {
            return jsonObjectHashMap.get(i).getJSONObject(NAME_OF_OBJECT_CONTAINING_ARRAY_OF_EXCHANGE_RATES).length();
        } catch (Exception e) {
            logger.error(BAD_REQUEST_IN_JSON_FILE_WRONG_NAME + "getKeyOfExchangeRatesArray at getObjectContainingArrayOfRates in EcbJsonNavigator index - " + i + "; name of object containing array of exchange rates: " + NAME_OF_OBJECT_CONTAINING_ARRAY_OF_EXCHANGE_RATES);
            throw new InternalServerErrorECB(UNVALID_RESPONSE_ECB_SERVER);
        }
    }

    private static JSONObject getObjectContainingArrayOfRates(Map<Integer, JSONObject> jsonObjectHashMap, int i) {
        try {
            return jsonObjectHashMap.get(i).getJSONObject(NAME_OF_OBJECT_CONTAINING_ARRAY_OF_EXCHANGE_RATES);
        } catch (Exception e) {
            logger.error(BAD_REQUEST_IN_JSON_FILE_WRONG_NAME + "objectContainingArrayOfRates at getObjectContainingArrayOfRates in EcbJsonNavigator index - " + i + "; name of object containing array of exchange rates: " + NAME_OF_OBJECT_CONTAINING_ARRAY_OF_EXCHANGE_RATES);
            throw new InternalServerErrorECB(UNVALID_RESPONSE_ECB_SERVER);
        }
    }

    private static int getLargestKey(Map<Integer, JSONObject> jsonObjectHashMap, int i) {
        List<String> sortedList = new ArrayList<>(jsonObjectHashMap.get(i).getJSONObject(NAME_OF_OBJECT_CONTAINING_ARRAY_OF_EXCHANGE_RATES).keySet());
        List<Integer> listOfParsedStrings = new ArrayList<>();
        for (String s : sortedList) {
            listOfParsedStrings.add(Integer.parseInt(s));
        }
        Collections.sort(listOfParsedStrings);
        return listOfParsedStrings.get(listOfParsedStrings.size() - 1);
    }

    private static JSONArray getJsonArrayFromJsonObject(JSONObject attributes, String keyOfJsonArray) throws JSONException {
        try {
            return attributes.getJSONArray(keyOfJsonArray);
        } catch (JSONException j) {
            logger.error(BAD_REQUEST_IN_JSON_FILE_WRONG_KEY + " - " + keyOfJsonArray);
            throw new InternalServerErrorECB(UNVALID_RESPONSE_ECB_SERVER);
        }
    }

    private static JSONObject getJsonObjectFromJsonArray(JSONArray dataSets, int index) throws JSONException {
        try {
            return dataSets.getJSONObject(index);
        } catch (JSONException j) {
            logger.error(BAD_REQUEST_IN_JSON_FILE_WRONG_INDEX + " - " + index);
            throw new InternalServerErrorECB(UNVALID_RESPONSE_ECB_SERVER);
        }
    }

    private static JSONObject getNestedJsonObject(JSONObject objectInsideDataSetsArray, String nameOfNestedObject) throws JSONException {
        try {
            return objectInsideDataSetsArray.getJSONObject(nameOfNestedObject);
        } catch (JSONException j) {
            logger.error(BAD_REQUEST_IN_JSON_FILE_WRONG_NAME + " - " + nameOfNestedObject);
            throw new InternalServerErrorECB(UNVALID_RESPONSE_ECB_SERVER);
        }
    }
}
