package com.currencyconverter.demo.builders.contracts;

import com.currencyconverter.demo.models.Currency;

public interface CurrencyBuilder {

   Currency reset();

   void setCurrencyValue(String value);

   void setCurrencyCode(String code);

   void setCurrencyName(String name);

   Currency getCurrency();
}
