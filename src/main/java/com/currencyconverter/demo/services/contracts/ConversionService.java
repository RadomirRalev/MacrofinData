package com.currencyconverter.demo.services.contracts;

import com.currencyconverter.demo.models.Conversion;

import java.time.LocalDate;
import java.util.List;

public interface ConversionService {
    List<Conversion> convertByDate(String from, String to, LocalDate date, String amount);

    List<Conversion> convertByTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit, String from, String to, String amount);

}
