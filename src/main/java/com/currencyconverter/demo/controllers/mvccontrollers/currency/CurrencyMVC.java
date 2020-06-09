package com.currencyconverter.demo.controllers.mvccontrollers.currency;

import com.currencyconverter.demo.models.mvcmodels.Currency;
import com.currencyconverter.demo.services.mvcservices.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.currencyconverter.demo.constants.ControllerConstants.*;
import static com.currencyconverter.demo.controllers.ParameterValidityChecker.checkIfAmountIsCorrect;

@Controller
public class CurrencyMVC {
    private CurrencyService currencyService;

    @Autowired
    public CurrencyMVC(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(CONVERT_PAGE_NAME)
    public String convertCurrency(@RequestParam String amount,
                                  @RequestParam String code1,
                                  @RequestParam String code2,
                                  Model model) {
        checkIfAmountIsCorrect(amount);
        List<Currency> currenciesList = currencyService.getAllCurrencies();
        String result = getConversionResult(amount, code1, code2);
        addAttributesToConvertModel(amount, code1, code2, model, currenciesList, result);
        return HOME_INDEX_PAGE_NAME;
    }

    private String getConversionResult(@RequestParam String amount,
                                       @RequestParam String code1,
                                       @RequestParam String code2) {
        String result;
        if (code1.equalsIgnoreCase(CONVERT_EURO)) {
            result = currencyService.getEuroToCurrencyByCode(amount, code2);
        } else if (code2.equalsIgnoreCase(CONVERT_EURO)) {
            result = currencyService.getCurrencyToEuroByCode(amount, code1);
        } else {
            result = currencyService.getCurrencyToCurrencyByCode(amount, code1, code2);
        }
        return result;
    }

    private void addAttributesToConvertModel(@RequestParam String amount,
                                             @RequestParam String code1,
                                             @RequestParam String code2,
                                             Model model,
                                             List<Currency> currenciesList,
                                             String result) {
        model.addAttribute(CONVERT_AMOUNT, amount);
        model.addAttribute(CONVERT_CURRENCY_FROM, currencyService.getByCode(code1));
        model.addAttribute(CONVERT_CURRENCY_TO, currencyService.getByCode(code2));
        model.addAttribute(CONVERT_CURRENCIES_LIST, currenciesList);
        model.addAttribute(CONVERT_CURRENCIES, result);
    }
}
