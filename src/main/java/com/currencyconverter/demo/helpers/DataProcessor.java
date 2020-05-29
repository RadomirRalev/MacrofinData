package com.currencyconverter.demo.helpers;

import com.currencyconverter.demo.api.JsonDataNavigator;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class DataProcessor {
    private static final int STARTING_INDEX_OF_TRIM_BEGINNING = 0;
    private static final int ENDING_INDEX_OF_TRIM_BEGINNING = 2;
    private static final int NUMBER_OF_INDICES_TO_BE_TRIMMED_ENDING = 8;

    public static Map<Integer, JSONObject> processKeysInCurrencyRateObject() {
        JSONObject series = JsonDataNavigator.getObjectContainingNestedRateObjects();
        Map<Integer, JSONObject> jsonObjectHashMap = new TreeMap<>();
        Iterator key = series.keys();
        while (key.hasNext()) {
            String k = key.next().toString();
            StringBuilder sb = new StringBuilder(k);
            sb.delete(STARTING_INDEX_OF_TRIM_BEGINNING, ENDING_INDEX_OF_TRIM_BEGINNING);
            sb.delete(k.length() - NUMBER_OF_INDICES_TO_BE_TRIMMED_ENDING, k.length());
            jsonObjectHashMap.put(Integer.parseInt(sb.toString()), (JSONObject) series.get(k));
        }
        return jsonObjectHashMap;
    }
}
