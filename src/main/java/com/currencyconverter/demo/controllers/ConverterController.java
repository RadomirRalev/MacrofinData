package com.currencyconverter.demo.controllers;

import com.currencyconverter.demo.models.Conversion;
import com.currencyconverter.demo.services.contracts.ConversionService;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.currencyconverter.demo.constants.ControllerConstants.REST_API_MAPPING;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.*;

@RestController
@Validated
@RequestMapping(REST_API_MAPPING)
public class ConverterController {
    private ConversionService conversionService;
    private CurrencyService currencyService;

    public ConverterController(ConversionService conversionService, CurrencyService currencyService) {
        this.conversionService = conversionService;
        this.currencyService = currencyService;
    }

    @CrossOrigin
    @GetMapping("/converter/timeseries")
    public List<Conversion> getCurrencyConverterTimeSeries(@RequestParam(defaultValue = "eur") String from,
                                                           @RequestParam(defaultValue = "eur") String to,
                                                           @RequestParam(required = false) String start_date,
                                                           @RequestParam(required = false) String end_date,
                                                           @RequestParam(required = false, defaultValue = "1") String page,
                                                           @RequestParam(required = false) String limit,
                                                           @RequestParam(required = false, defaultValue = "1.0") String amount) {
        checkIfCurrencyIsCorrect(from, currencyService.getAvailableCurrencies(false));
        checkIfCurrencyIsCorrect(to, currencyService.getAvailableCurrencies(false));
        ArrayList<String> fromArr = getListOfMultipleCurrencyCodes(from);
        ArrayList<String> toArr = getListOfMultipleCurrencyCodes(to);
        if (!amount.equalsIgnoreCase("1.0")) {
            checkIfAmountIsCorrect(amount);
        }
        LocalDate localDateFrom = checkDateParameter(start_date);
        LocalDate localDateTo = checkDateParameter(end_date);
        checkIfFromDateIsBeforeToDate(localDateFrom, localDateTo);
        page = checkIfPageIsCorrect(page);
        limit = checkIfLimitIsCorrect(limit);
        return conversionService.convertByTimeSeries(localDateFrom, localDateTo, page, limit, fromArr, toArr, amount);
    }

    @CrossOrigin
    @GetMapping("/converter")
    public List<Conversion> getCurrencyConverter(@RequestParam(defaultValue = "eur") String from,
                                                 @RequestParam(defaultValue = "eur") String to,
                                                 @RequestParam(required = false) String date,
                                                 @RequestParam(required = false, defaultValue = "1.0") String amount) {
        checkIfCurrencyIsCorrect(from, currencyService.getAvailableCurrencies(false));
        checkIfCurrencyIsCorrect(to, currencyService.getAvailableCurrencies(false));
        ArrayList<String> fromArr = getListOfMultipleCurrencyCodes(from);
        ArrayList<String> toArr = getListOfMultipleCurrencyCodes(to);
        if (!amount.equalsIgnoreCase("1.0")) {
            checkIfAmountIsCorrect(amount);
        }
        try {
            if (date == null) {
                return conversionService.convertByDate(fromArr, toArr, LocalDate.now(), amount);
            }
            LocalDate localDate = checkDateParameter(date);
            return conversionService.convertByDate(fromArr, toArr, localDate, amount);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
