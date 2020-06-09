package com.currencyconverter.demo.api;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;
import static com.currencyconverter.demo.api.QueryBuilder.getTodayCurrencyRatesURI;

public class ApiConsumer {

    public static JSONObject getCurrencyExchangeDataFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        return new JSONObject(restTemplate.getForObject(getTodayCurrencyRatesURI(), String.class));
    }
}

//    getTodayCurrencyRatesURI()
//https://sdw-wsrest.ecb.europa.eu/service/data/EXR/D..EUR.SP00.A?startPeriod=2020-05-06&endPeriod=2020-05-06