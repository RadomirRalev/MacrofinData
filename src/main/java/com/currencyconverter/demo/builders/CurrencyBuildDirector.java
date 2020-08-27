package com.currencyconverter.demo.builders;

import com.currencyconverter.demo.builders.contracts.CurrencyBuilder;

public class CurrencyBuildDirector {
    CurrencyBuilder currencyBuilder;

    public CurrencyBuildDirector(CurrencyBuilder currencyBuilder) {
        this.currencyBuilder = currencyBuilder;
    }

    public void constructECBCurrency(String value, String code, String currencyName) {
        currencyBuilder.reset();
        currencyBuilder.setCurrencyValue(value);
        currencyBuilder.setCurrencyCode(code);
        currencyBuilder.setCurrencyName(currencyName);
    }
}
