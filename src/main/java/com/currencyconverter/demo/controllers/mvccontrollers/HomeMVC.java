package com.currencyconverter.demo.controllers.mvccontrollers;

import com.currencyconverter.demo.models.Currency;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import static com.currencyconverter.demo.constants.ControllerConstants.HOME_CURRENCIES_LIST;
import static com.currencyconverter.demo.constants.ControllerConstants.HOME_INDEX_PAGE_NAME;

@Controller("/")
public class HomeMVC {
    private CurrencyService currencyService;

    @Autowired
    public HomeMVC(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public String showHomePage(Model model) {
        List<Currency> currenciesList = currencyService.getAllCurrencies();
        model.addAttribute(HOME_CURRENCIES_LIST, currenciesList);
        return HOME_INDEX_PAGE_NAME;
    }
}
