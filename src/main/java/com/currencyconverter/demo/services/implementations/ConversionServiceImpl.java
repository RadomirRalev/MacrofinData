package com.currencyconverter.demo.services.implementations;

import com.currencyconverter.demo.models.Conversion;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.services.contracts.ConversionService;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ConversionServiceImpl implements ConversionService {
    public CurrencyService currencyService;

    public ConversionServiceImpl(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public List<Conversion> convertByDate(String from, String to, LocalDate date, String amount) {
        CurrencyCollection collection = currencyService.getByCode(to, from);
        List<Conversion> resultList = new ArrayList<>();
        HashMap<String, String> query = new HashMap<>();
        query.put("from", from);
        query.put("to", to);
        query.put("amount", amount);
        String rate = collection.getList().get(0).getValue();
        String dateOfConversion = date.toString();
        double result = Double.parseDouble(rate)*Double.parseDouble(amount);
        Conversion conversion = new Conversion(query, rate, dateOfConversion, Double.toString(result));
        resultList.add(conversion);
        return resultList;
    }

    @Override
    public List<Conversion> convertByTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, String from, String to, String amount) {
        List<CurrencyCollection> collectionList = currencyService.getTimeSeries(localDateFrom, localDateTo, page, limit, from, to);
        List<Conversion> resultList = new ArrayList<>();
        for (int i = 0; i < collectionList.size(); i++) {
            HashMap<String, String> query = new HashMap<>();
            query.put("from", from.toUpperCase());
            query.put("to", to.toUpperCase());
            query.put("amount", amount);
            String rate = collectionList.get(i).getList().get(0).getValue();
            String dateOfConversion = collectionList.get(i).getDate();
            double result = Double.parseDouble(rate)*Double.parseDouble(amount);
            Conversion conversion = new Conversion(query, rate, dateOfConversion, Double.toString(result));
            resultList.add(conversion);
        }
        return resultList;
    }
}
