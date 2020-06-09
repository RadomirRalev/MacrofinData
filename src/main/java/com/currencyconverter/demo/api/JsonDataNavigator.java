package com.currencyconverter.demo.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.currencyconverter.demo.constants.ApiConstants.*;

public class JsonDataNavigator {

    public static JSONObject getObjectContainingNestedRateObjects() {
        JSONArray dataSets = getJsonArrayFromJsonObject(ApiConsumer.getCurrencyExchangeDataFromApi(), KEY_OF_CURRENCY_DATA_ARRAY);
        JSONObject objectContainingExchangeRateSeries = getJsonObjectFromJsonArray(dataSets, INDEX_OF_OBJECT_CONTAINING_EXCHANGE_RATE_SERIES);
        return getNestedJsonObject(objectContainingExchangeRateSeries, NAME_OF_OBJECT_CONTAINING_NESTED_EXCHANGE_RATE_OBJECTS);
    }

    public static JSONArray getArrayOfCurrencyCodeObjects() {
        JSONObject structure = getNestedJsonObject(ApiConsumer.getCurrencyExchangeDataFromApi(), NAME_OF_OBJECT_CONTAINING_DESCRIPTION_OF_DATA);
        JSONObject attributes = getNestedJsonObject(structure, NAME_OF_OBJECT_CONTAINING_ATTRIBUTES_OF_DATA);
        JSONArray series = getJsonArrayFromJsonObject(attributes, KEY_OF_CURRENCY_CODES_ARRAY);
        JSONObject objectInsideSeriesArray = getJsonObjectFromJsonArray(series, INDEX_OF_OBJECT_CONTAINING_CURRENCY_CODES_ARRAY);
        return getJsonArrayFromJsonObject(objectInsideSeriesArray, KEY_OF_ARRAY_CONTAINING_THREE_LETTER_CURRENCY_CODE);
    }

    public static String getCurrencyCodeString(JSONArray values, int i) {
        JSONObject jsonObjArr = new JSONObject(values.getJSONObject(i).toString());
        return jsonObjArr.getString(KEY_OF_CURRENCY_CODE_IN_OBJECT);
    }

    public static String getCurrencyNameString(JSONArray values, int i) {
        JSONObject jsonObjArr = new JSONObject(values.getJSONObject(i).toString());
        return jsonObjArr.getString(KEY_OF_CURRENCY_NAME_IN_OBJECT);
    }

    public static String getCurrencyExchangeRate(Map<Integer, JSONObject> jsonObjectHashMap, int i) {
        JSONObject objectContainingArrayOfRates = jsonObjectHashMap.get(i).getJSONObject(NAME_OF_OBJECT_CONTAINING_ARRAY_OF_EXCHANGE_RATES);
        JSONArray arr = objectContainingArrayOfRates.getJSONArray(KEY_OF_ARRAY_CONTAINING_EXCHANGE_RATES);
        Double rate = arr.getDouble(INDEX_OF_OBJECT_CONTAINING_CURRENCY_RATE);
        return rate.toString();
    }

    private static JSONArray getJsonArrayFromJsonObject(JSONObject attributes, String keyOfJsonArray) throws JSONException {
        return attributes.getJSONArray(keyOfJsonArray);
    }

    private static JSONObject getJsonObjectFromJsonArray(JSONArray dataSets, int index) throws JSONException {
        return dataSets.getJSONObject(index);
    }

    private static JSONObject getNestedJsonObject(JSONObject objectInsideDataSetsArray, String nameOfNestedObject) throws JSONException {
        return objectInsideDataSetsArray.getJSONObject(nameOfNestedObject);
    }
}
