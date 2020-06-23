package com.currencyconverter.demo.controllers;

import com.currencyconverter.demo.CurrencyConverterStart;
import com.currencyconverter.demo.helpers.ParameterValidityChecker;
import com.currencyconverter.demo.models.CurrencyCollection;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;

import static com.currencyconverter.demo.constants.ControllerConstants.REST_API_MAPPING;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.*;

@RestController
@Validated
@RequestMapping(REST_API_MAPPING)
public class CurrencyController {
    private CurrencyService currencyService;
    public Logger logger = LoggerFactory.getLogger(ParameterValidityChecker.class);


    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/newest")
    public CurrencyCollection getLatestRates(@RequestParam(required = false) String codes) {
        try {
            if (codes != null) {
                return currencyService.getByCode(codes);
            }
            return currencyService.getAllCurrencies();
        } catch (EntityNotFoundException e) {
            logger.error("EntityNotFoundException at getLatestRates method in CurrencyController with codes" + " " + codes);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/available-currencies")
    public Map<String, Map<String, String>> getAvailableCurrencies() {
        try {
            return currencyService.getAvailableCurrencies();
        } catch (EntityNotFoundException e) {
            logger.error("EntityNotFoundException at getAvailableCurrencies method in CurrencyController");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/historical")
    public CurrencyCollection getCurrenciesPerDate(@RequestParam String date) {
        try {
            return currencyService.getCurrenciesPerDate(date);
        } catch (EntityNotFoundException e) {
            logger.error("EntityNotFoundException at getCurrenciesPerDate method in CurrencyController with date" + " - " + date);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/timeseries")
    public List<CurrencyCollection> getTimeSeries(@RequestParam String from,
                                                  @RequestParam String to,
    @RequestParam(defaultValue = "1") String page,
    @RequestParam(required = false) String limit) {
        page = checkIfPageIsCorrect(page);
        limit = checkIfLimitIsCorrect(limit);
        try {
            return currencyService.getTimeSeries(from, to, page, limit);
        } catch (EntityNotFoundException e) {
            logger.error("EntityNotFoundException at getTimeSeries method in CurrencyController with " +
                    "dateFrom" + " - " + from + " " +
                    "dateTo" + " - " + to + " " +
                    "page" + " - " + page + " " +
                    "limit" + " - " + limit);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getcurrencytoeurobycode")
    public String getCurrencyToEuroByCode(@RequestParam String amount,
                                          @RequestParam String code) {
        checkIfAmountIsCorrect(amount);
        checkIfCodePathVariableIsCorrect(code);
        try {
            return currencyService.getCurrencyToEuroByCode(amount, code);
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}