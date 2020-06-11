package com.currencyconverter.demo.controllers;

import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.services.contracts.CurrencyService;
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

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = "/getallcurrencies")
    public Map<String, List<Currency>> getAllCurrencies() {
        try {
            return currencyService.getAllCurrencies();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getbycode/{code}")
    public Map<String, Currency> getByCode(@PathVariable String code) {
        checkIfCodePathVariableIsCorrect(code);
        try {
            return currencyService.getByCode(code);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getpairbycode")
    public Map<String, Currency[]> getPairByCode(@RequestParam String code1,
                                                 @RequestParam String code2) {
        checkIfCodePathVariableIsCorrect(code1);
        checkIfCodePathVariableIsCorrect(code2);
        try {
            return currencyService.getPairByCode(code1, code2);
        } catch (EntityNotFoundException e) {
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