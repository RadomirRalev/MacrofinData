package com.currencyconverter.demo.models;

import java.util.HashMap;

public class Conversion {
    private HashMap<String, String> query;
    private String rate;
    private String date;
    private String result;
    private String source;

    public Conversion(HashMap<String, String> query, String rate, String date, String result) {
        this.query = query;
        this.rate = rate;
        this.date = date;
        this.result = result;
    }

    public HashMap<String, String> getQuery() {
        return query;
    }

    private void setQuery(HashMap<String, String> query) {
        this.query = query;
    }

    public String getRate() {
        return rate;
    }

    private void setRate(String rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    private void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    private void setResult(String result) {
        this.result = result;
    }

    public String getSource() {
        return "European Central Bank";
    }

    private void setSource(String source) {
        this.source = source;
    }
}
