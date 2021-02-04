package com.currencyconverter.demo.controllers;

import com.currencyconverter.demo.helpers.ParameterValidityChecker;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.currencyconverter.demo.constants.ControllerConstants.REST_API_MAPPING;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.*;

@CrossOrigin
@RestController
@Validated
@RequestMapping(REST_API_MAPPING)
public class CurrencyController {
    private CurrencyService currencyService;
    public Logger logger = LoggerFactory.getLogger(ParameterValidityChecker.class);


    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @CrossOrigin
    @GetMapping("/newest")
    public CurrencyCollection getLatestRates(@RequestParam(required = false, defaultValue = "all") String codes,
                                             @RequestParam(required = false, defaultValue = "eur") String base) {
        checkIfBaseIsCorrect(base.toUpperCase());
        ArrayList<String> listOfCurrencyCodes = getListOfMultipleCurrencyCodes(codes.toUpperCase());
        try {
            return currencyService.getCurrenciesPerSingleDay(LocalDate.now(), listOfCurrencyCodes, base);
        } catch (Exception e) {
            logger.error("ResponseStatusException at getLatestRates method in CurrencyController with codes" + " " + codes + " and base " + base);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/available-currencies")
    public Map<String, Map<String, String>> getAvailableCurrencies(@RequestParam(required = false, defaultValue = "false") boolean ever) {
        try {
            return currencyService.getAvailableCurrencies(ever);
        } catch (Exception e) {
            logger.error("ResponseStatusException at getAvailableCurrencies method in CurrencyController");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/history")
    public CurrencyCollection getCurrenciesPerDate(@RequestParam(required = false) String date,
                                                   @RequestParam(required = false, defaultValue = "all") String codes,
                                                   @RequestParam(required = false, defaultValue = "eur") String base) {
        LocalDate localDate = checkDateParameter(date);
        checkIfBaseIsCorrect(base.toUpperCase());
        ArrayList<String> listOfCurrencyCodes = getListOfMultipleCurrencyCodes(codes.toUpperCase());
        try {
            return currencyService.getCurrenciesPerSingleDay(localDate, listOfCurrencyCodes, base);
        } catch (Exception e) {
            logger.error("ResponseStatusException at getCurrenciesPerDate method in CurrencyController with date" + " - " + date);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @CrossOrigin
    @GetMapping("/timeseries")
    public List<CurrencyCollection> getTimeSeries(@RequestParam(defaultValue = "all", required = false) String codes,
                                                  @RequestParam String start,
                                                  @RequestParam String end,
                                                  @RequestParam(defaultValue = "1") String page,
                                                  @RequestParam(required = false) String limit,
                                                  @RequestParam(required = false, defaultValue = "eur") String base) {
        LocalDate localDateFrom = checkDateParameter(start.toUpperCase());
        LocalDate localDateTo = checkDateParameter(end.toUpperCase());
        checkIfFromDateIsBeforeToDate(localDateFrom, localDateTo);
        page = checkIfPageIsCorrect(page);
        limit = checkIfLimitIsCorrect(limit);
        checkIfBaseIsCorrect(base.toUpperCase());
        ArrayList<String> listOfCurrencyCodes = getListOfMultipleCurrencyCodes(codes);
        try {
            return currencyService.getTimeSeries(localDateFrom, localDateTo, page, limit, base, listOfCurrencyCodes);
        } catch (Exception e) {
            logger.error("ResponseStatusException at getTimeSeries method in CurrencyController with " +
                    "dateFrom" + " - " + start + " " +
                    "dateTo" + " - " + end + " " +
                    "page" + " - " + page + " " +
                    "limit" + " - " + limit);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @CrossOrigin
    private void checkIfBaseIsCorrect(@RequestParam(required = false, defaultValue = "eur") String base) {
        if (!base.equalsIgnoreCase("eur")) {
            checkIfCodePathVariableIsCorrect(base);
            checkIfCurrencyIsCorrect(base, getAvailableCurrencies(false));
        }
    }
}