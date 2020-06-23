package com.currencyconverter.demo.repository.contracts;

import org.json.JSONArray;
import org.json.JSONObject;

public interface CurrencyRepository {

    JSONObject getCurrencyExchangeData();

    JSONObject getCurrencyExchangeData(String date);
}
