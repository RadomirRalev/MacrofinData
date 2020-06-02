package com.currencyconverter.demo.controllers.mvccontrollers.currency;

import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CurrencyMVC {
    private CurrencyService currencyService;

    @Autowired
    public CurrencyMVC(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/convert")
    public String convertCurrency(@RequestParam String amount,
                                  @RequestParam String code1,
                                  @RequestParam String code2,
                                  Model model) {
        List<Currency> currenciesList = currencyService.getAllCurrencies();
        String result;
        if (code1.equalsIgnoreCase("eur")) {
            result = currencyService.getEuroToCurrencyByCode(amount, code2);
        } else if (code2.equalsIgnoreCase("eur")) {
            result = currencyService.getCurrencyToEuroByCode(amount, code1);
        } else {
            result = currencyService.getCurrencyToCurrencyByCode(amount, code1, code2);
        }
        model.addAttribute("amount", amount);
        model.addAttribute("currencyFrom", currencyService.getByCode(code1));
        model.addAttribute("currencyTo", currencyService.getByCode(code2));
        model.addAttribute("currenciesList", currenciesList);
        model.addAttribute("currencies", result);
        return "index";
    }
}