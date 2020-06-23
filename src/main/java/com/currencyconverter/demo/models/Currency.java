package com.currencyconverter.demo.models;

public class Currency {
    private String code;
    private String value;
    private String currencyName;
    private String source;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getSource() {
        return "European Central Bank";
    }
}
