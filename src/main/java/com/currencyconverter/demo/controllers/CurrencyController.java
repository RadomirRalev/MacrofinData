package com.currencyconverter.demo.controllers;

import com.currencyconverter.demo.helpers.ParameterValidityChecker;
import com.currencyconverter.demo.models.Conversion;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.services.contracts.ConversionService;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.currencyconverter.demo.constants.ControllerConstants.REST_API_MAPPING;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.*;

@RestController
@Validated
@RequestMapping(REST_API_MAPPING)
public class CurrencyController {
    private CurrencyService currencyService;
    private ConversionService conversionService;
    public Logger logger = LoggerFactory.getLogger(ParameterValidityChecker.class);


    public CurrencyController(CurrencyService currencyService,
                              ConversionService conversionService) {
        this.currencyService = currencyService;
        this.conversionService = conversionService;
    }

    @GetMapping("/newest")
    public CurrencyCollection getLatestRates(@RequestParam(required = false, defaultValue = "all") String codes,
                                             @RequestParam(required = false, defaultValue = "eur") String base) {
        if (!base.equals("eur")) {
            checkIfCurrencyIsCorrect(base, getAvailableCurrencies(false));
        }
        try {
            return currencyService.getByCode(codes, base);
        } catch (Exception e) {
            logger.error("EntityNotFoundException at getLatestRates method in CurrencyController with codes" + " " + codes);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/available-currencies")
    public Map<String, Map<String, String>> getAvailableCurrencies(@RequestParam(required = false, defaultValue = "false") boolean ever) {
        try {
            return currencyService.getAvailableCurrencies(ever);
        } catch (Exception e) {
            logger.error("EntityNotFoundException at getAvailableCurrencies method in CurrencyController");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/historical")
    public CurrencyCollection getCurrenciesPerDate(@RequestParam String date,
                                                   @RequestParam(required = false, defaultValue = "all") String codes,
                                                   @RequestParam(required = false, defaultValue = "eur") String base) {
        LocalDate localDate = checkDateParameter(date);
        if (!base.equals("eur")) {
            checkIfCurrencyIsCorrect(base, getAvailableCurrencies(false));
        }
        try {
            return currencyService.getCurrenciesPerDate(localDate, codes, base);
        } catch (Exception e) {
            logger.error("EntityNotFoundException at getCurrenciesPerDate method in CurrencyController with date" + " - " + date);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/timeseries")
    public List<CurrencyCollection> getTimeSeries(@RequestParam(defaultValue = "all", required = false) String codes,
                                                  @RequestParam String from,
                                                  @RequestParam String to,
                                                  @RequestParam(defaultValue = "1") String page,
                                                  @RequestParam(required = false) String limit,
                                                  @RequestParam(required = false, defaultValue = "eur") String base) {
        LocalDate localDateFrom = checkDateParameter(from);
        LocalDate localDateTo = checkDateParameter(to);
        checkIfFromDateIsBeforeToDate(localDateFrom, localDateTo);
        page = checkIfPageIsCorrect(page);
        limit = checkIfLimitIsCorrect(limit);
        if (!base.equals("eur")) {
            checkIfCurrencyIsCorrect(base, getAvailableCurrencies(false));
        }
        try {
            return currencyService.getTimeSeries(localDateFrom, localDateTo, page, limit, base, codes);
        } catch (Exception e) {
            logger.error("EntityNotFoundException at getTimeSeries method in CurrencyController with " +
                    "dateFrom" + " - " + from + " " +
                    "dateTo" + " - " + to + " " +
                    "page" + " - " + page + " " +
                    "limit" + " - " + limit);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/converter")
    public List<Conversion> getCurrencyConverter(@RequestParam(defaultValue = "eur") String from,
                                                 @RequestParam(defaultValue = "eur") String to,
                                                 @RequestParam(required = false) String specificDate,
                                                 @RequestParam(required = false) String start_date,
                                                 @RequestParam(required = false) String end_date,
                                                 @RequestParam(required = false, defaultValue = "1") String page,
                                                 @RequestParam(required = false) String limit,
                                                 @RequestParam(required = false, defaultValue = "1.0") String amount) {
        checkIfCurrencyIsCorrect(from, getAvailableCurrencies(false));
        checkIfCurrencyIsCorrect(to, getAvailableCurrencies(false));
        if (!amount.equalsIgnoreCase("1.0")) {
            checkIfAmountIsCorrect(amount);
        }
        if (specificDate != null) {
            LocalDate localDate = checkDateParameter(specificDate);
            return conversionService.convertByDate(from, to, localDate, amount);
        } else if (start_date == null) {
            return conversionService.convertByDate(from, to, LocalDate.now(), amount);
        }
        LocalDate localDateFrom = checkDateParameter(start_date);
        LocalDate localDateTo = checkDateParameter(end_date);
        checkIfFromDateIsBeforeToDate(localDateFrom, localDateTo);
        page = checkIfPageIsCorrect(page);
        limit = checkIfLimitIsCorrect(limit);
        return conversionService.convertByTimeSeries(localDateFrom, localDateTo, page, limit, from, to, amount);
    }

    @GetMapping("/getcurrencytoeurobycode")
    public String getCurrencyToEuroByCode(@RequestParam String amount,
                                          @RequestParam String code) {
        checkIfAmountIsCorrect(amount);
        checkIfCodePathVariableIsCorrect(code);
        try {
            return currencyService.getCurrencyToEuroByCode(amount, code);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/geteurotocurrencybycode")
    public String getEuroToCurrencyByCode(@RequestParam String amount,
                                          @RequestParam String code) {
        checkIfAmountIsCorrect(amount);
        checkIfCodePathVariableIsCorrect(code);
        try {
            return currencyService.getEuroToCurrencyByCode(amount, code);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getcurrencytocurrencybycode")
    public String getCurrencyToCurrencyByCode(@RequestParam String amount,
                                              @RequestParam String code1,
                                              @RequestParam String code2) {
        checkIfAmountIsCorrect(amount);
        checkIfCodePathVariableIsCorrect(code1);
        checkIfCodePathVariableIsCorrect(code2);
        try {
            return currencyService.getCurrencyToCurrencyByCode(amount, code1, code2);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}