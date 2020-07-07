package com.currencyconverter.demo.services.contracts;

import com.currencyconverter.demo.models.Conversion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface ConversionService {
    List<Conversion> convertByDate(ArrayList<String> from, ArrayList<String> to, LocalDate date, String amount);

    List<Conversion> convertByTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, ArrayList<String> from, ArrayList<String> to, String amount);

}
