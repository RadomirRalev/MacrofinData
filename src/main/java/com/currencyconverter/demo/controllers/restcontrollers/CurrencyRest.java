package com.currencyconverter.demo.controllers.restcontrollers;

import com.currencyconverter.demo.exceptions.BadParameterException;
import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@RestController
@Validated
@RequestMapping("api/currency")
public class CurrencyRest {
    private CurrencyService currencyService;

    public CurrencyRest(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/getbyid/{id}")
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

    @GetMapping(value = "/updaterecords",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateRecords() {
        try {
            currencyService.updateRecords();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private int processIdPathVariable(String id) {
        int idNumber;
        try {
            idNumber = Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            throw new BadParameterException("Id must be a number");
        }
        if (idNumber < 0) {
            throw new BadParameterException("Id cannot be less than 0");
        }
        return idNumber;
    }

    private void checkIfCodePathVariableIsCorrect(@PathVariable String code) {
        if (code.length() != 3) {
            throw new BadParameterException("ISO code must contain exactly three letters");
        }
        for (int i = 0; i < code.length(); i++) {
            if ((code.charAt(i) < 65 || code.charAt(i) > 122) || (code.charAt(i) < 96 && code.charAt(i) > 90)) {
                throw new BadParameterException("ISO code must contain only letters");
            }
        }
    }

    private void checkIfAmountIsCorrect(@RequestParam String amount) {
        for (int i = 0; i < amount.length(); i++) {
            if ((amount.charAt(i) < 48 || amount.charAt(i) > 57) && amount.charAt(i) != 46) {
                throw new BadParameterException("Amount must be an integer or decimal number");
            }
        }
    }
}