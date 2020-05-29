package com.currencyconverter.demo.controllers.restcontrollers;

import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@RequestMapping("api/currency")
public class CurrencyRest {
    private CurrencyService currencyService;

    public CurrencyRest(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getbyid/{id}")
    public Currency getById(@PathVariable @PositiveOrZero int id) {
        try {
            return currencyService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getbycode/{code}")
    public Currency getByCode(@PathVariable String code) {
        try {
            return currencyService.getByCode(code);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getpairbyid")
    public Currency[] getPair(@RequestParam int id1,
                              @RequestParam int id2) {
        try {
            return currencyService.getPairById(id1, id2);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getpairbycode")
    public Currency[] getPair(@RequestParam String code1,
                              @RequestParam String code2) {
        try {
            return currencyService.getPairByCode(code1, code2);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getcurrencytoeurobycode")
    public String getCurrencyToEuroByCode(@RequestParam String amount,
                                          @RequestParam String code) {
        try {
            return currencyService.getCurrencyToEuroByCode(amount, code);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/geteurotocurrencybycode")
    public String getEuroToCurrencyByCode(@RequestParam String amount,
                                          @RequestParam String code) {
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

//    @GetMapping(value = "/getapi",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public String getApi() {
//        try {
//            return currencyService.getApi();
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//    @GetMapping(value = "/getcodes",
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public String getCodes() {
//        try {
//            return currencyService.getCurrencyCodes();
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
}
