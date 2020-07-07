package com.currencyconverter.demo.services.implementations;

import com.currencyconverter.demo.models.Conversion;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.services.contracts.ConversionService;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.currencyconverter.demo.helpers.MoneyFormatter.formatSum;

@Service
public class ConversionServiceImpl implements ConversionService {
    public CurrencyService currencyService;

    public ConversionServiceImpl(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public List<Conversion> convertByDate(ArrayList<String> from, ArrayList<String> to, LocalDate date, String amount) {
        CurrencyCollection collection = currencyService.getCurrenciesPerSingleDay(date, to, from.get(0));
        List<Conversion> resultList = new ArrayList<>();
        String rate = collection.getList().get(0).getValue();
        String dateOfConversion = collection.getDate();
        setConversionValue(from.get(0), to.get(0), amount, resultList, rate, dateOfConversion);
        return resultList;
    }

    @Override
    public List<Conversion> convertByTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, ArrayList<String> from, ArrayList<String> to, String amount) {
        List<CurrencyCollection> collectionList = currencyService.getTimeSeries(localDateFrom, localDateTo, page, limit, from.get(0), to);
        List<Conversion> resultList = new ArrayList<>();
        for (CurrencyCollection currencyCollection : collectionList) {
            String rate = currencyCollection.getList().get(0).getValue();
            String dateOfConversion = currencyCollection.getDate();
            setConversionValue(from.get(0), to.get(0), amount, resultList, rate, dateOfConversion);
        }
        return resultList;
    }

    private static void setConversionValue(String from, String to, String amount, List<Conversion> resultList, String rate, String dateOfConversion) {
        Conversion conversion;
        HashMap<String, String> query = new HashMap<>();
        query.put("from", from.toUpperCase());
        query.put("to", to.toUpperCase());
        query.put("amount", amount);
        if (!rate.equalsIgnoreCase("service unavailable")) {
            double result = Double.parseDouble(rate) * Double.parseDouble(amount);
            BigDecimal bd = formatSum(result);
            conversion = new Conversion(query, rate, dateOfConversion, bd.toString());
        } else {
            conversion = new Conversion(query, rate, dateOfConversion, rate);
        }
        resultList.add(conversion);
    }
}
