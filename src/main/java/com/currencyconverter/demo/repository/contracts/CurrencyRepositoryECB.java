package com.currencyconverter.demo.repository.contracts;

import org.json.JSONObject;

public interface CurrencyRepositoryECB {

    JSONObject getCurrencyExchangeData();

    JSONObject getCurrencyExchangeData(String date);

    JSONObject getCurrencyExchangeData(String from, String to);

}
