package com.currencyconverter.demo.controllers.restcontrollers;

import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

import static com.currencyconverter.demo.controllers.parameterValidityChecker.*;

@RestController
@Validated
@RequestMapping("api/currency")
public class CurrencyRest {
    private CurrencyService currencyService;

    public CurrencyRest(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = "/getbyid/{id}", produces = "application/json")
    public Currency getById(@PathVariable("id") String id) {
        int idNumber = processIdPathVariable(id);
        try {
            return currencyService.getById(idNumber);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getbycode/{code}")
    public Currency getByCode(@PathVariable String code) {
        checkIfCodePathVariableIsCorrect(code);
        try {
            return currencyService.getByCode(code);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getpairbyid")
    public Currency[] getPairById(@RequestParam String id1,
                                  @RequestParam String id2) {
        int idNumber1 = processIdPathVariable(id1);
        int idNumber2 = processIdPathVariable(id2);
        try {
            return currencyService.getPairById(idNumber1, idNumber2);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getpairbycode")
    public Currency[] getPairByCode(@RequestParam String code1,
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

    @GetMapping(value = "/getcurrencytocurrencybycode", produces = "application/json")
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

    @RequestMapping(value = "/updaterecords",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateRecords() {
        try {
            currencyService.updateRecords();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}