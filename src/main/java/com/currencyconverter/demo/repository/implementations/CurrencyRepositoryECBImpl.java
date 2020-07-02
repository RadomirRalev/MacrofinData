package com.currencyconverter.demo.repository.implementations;

import com.currencyconverter.demo.exceptions.server.BadGatewayException;
import com.currencyconverter.demo.helpers.ParameterValidityChecker;
import com.currencyconverter.demo.repository.contracts.CurrencyRepositoryECB;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import static com.currencyconverter.demo.constants.ExceptionConstants.*;
import static com.currencyconverter.demo.repository.querybuilders.ecb.EcbQueryBuilder.getCurrencyRatesURI;

@Repository
public class CurrencyRepositoryECBImpl implements CurrencyRepositoryECB {
    public static Logger logger = LoggerFactory.getLogger(ParameterValidityChecker.class);

    @Override
    public JSONObject getCurrencyExchangeData() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return new JSONObject(restTemplate.getForObject(getCurrencyRatesURI(), String.class));
        } catch (Exception e) {
            logger.error("BadGatewayException at URI " + getCurrencyRatesURI());
            throw new BadGatewayException(NO_CONNECTION_WITH_ECB_SERVER);
        }
    }

    @Override
    public JSONObject getCurrencyExchangeData(String date) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return new JSONObject(restTemplate.getForObject(getCurrencyRatesURI(date), String.class));
        } catch (Exception e) {
            logger.error("BadGatewayException at URI " + getCurrencyRatesURI());
            throw new BadGatewayException(NO_CONNECTION_WITH_ECB_SERVER);
        }
    }

    @Override
    public JSONObject getCurrencyExchangeData(String from, String to) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return new JSONObject(restTemplate.getForObject(getCurrencyRatesURI(from, to), String.class));
        } catch (Exception e) {
            logger.error("BadGatewayException at URI " + getCurrencyRatesURI());
            throw new BadGatewayException(NO_CONNECTION_WITH_ECB_SERVER);
        }
    }
}

