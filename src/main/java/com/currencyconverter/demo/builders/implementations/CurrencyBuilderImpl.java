package com.currencyconverter.demo.builders.implementations;

import com.currencyconverter.demo.builders.contracts.CurrencyBuilder;
import com.currencyconverter.demo.models.Currency;

public class CurrencyBuilderImpl implements CurrencyBuilder {
    private Currency currency;

    public CurrencyBuilderImpl() {
        currency = this.reset();
    }

    @Override
    public Currency reset() {
        return new Currency();
    }

    @Override
    public void setCurrencyValue(String value) {
        this.currency.setValue(value);
    }

    @Override
    public void setCurrencyCode(String code) {
        this.currency.setCode(code);
    }

    @Override
    public void setCurrencyName(String name) {
        this.currency.setCurrencyName(name);
    }

    public Currency getCurrency() {
        Currency currency = this.currency;
        this.reset();
        return currency;
    }
}
