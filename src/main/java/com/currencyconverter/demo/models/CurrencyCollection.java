package com.currencyconverter.demo.models;

import java.util.List;

public class CurrencyCollection {
    private String base;
    private String date;
    private List<Currency> list;

    private CurrencyCollection() {
        base = "EUR";
    }

    public CurrencyCollection(String date, List<Currency> list) {
        this();
        this.date = date;
        this.list = list;
    }

    public CurrencyCollection(String date, List<Currency> list, String base) {
        this();
        this.date = date;
        this.list = list;
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Currency> getList() {
        return list;
    }

    public void setList(List<Currency> list) {
        this.list = list;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
