package com.currencyconverter.demo.controllers;

import com.currencyconverter.demo.exceptions.client.BadParameterException;
import com.currencyconverter.demo.exceptions.client.ResourceNotFoundException;
import com.currencyconverter.demo.exceptions.client.UnprocessableEntityException;
import com.currencyconverter.demo.services.contracts.CurrencyService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CurrencyControllerTests {

    @InjectMocks
    private CurrencyController currencyController;
    @Mock
    private CurrencyService currencyService;

    @Test
    public void contextLoads() throws Exception {
        Assert.assertNotNull(currencyController);
    }

    @Test
    public void getLatestRatestCallsCurrencyServiceWhenNoParametersArePassed() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("all");
        String base = "eur";
        currencyController.getLatestRates(codes.get(0), base);
        Mockito.verify(currencyService, Mockito.times(1)).getCurrenciesPerSingleDay(LocalDate.now(), codes, base);
    }

    @Test
    public void getLatestRatestCallsCurrencyServiceWhenSingleCodeParameterIsPassed() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("BGN");
        String base = "eur";
        currencyController.getLatestRates(codes.get(0), base);
        Mockito.verify(currencyService, Mockito.times(1)).getCurrenciesPerSingleDay(LocalDate.now(), codes, base);
    }

    @Test
    public void getLatestRatestCallsCurrencyServiceWhenMultipleCodeParameterArePassed() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("BGN");
        codes.add("DZD");
        codes.add("USD");
        String base = "eur";
        currencyController.getLatestRates("BGN,DZD,USD", base);
        Mockito.verify(currencyService, Mockito.times(1)).getCurrenciesPerSingleDay(LocalDate.now(), codes, base);
    }

    @Test
    public void getLatestRatestCallsCurrencyServiceWhenNoCodeParameterAndBaseArePassed() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("all");
        String base = "DZD";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("DZD", "Algerian dinar");
        availableCurrencies.put("currencies", currency);
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates(codes.get(0), base);
        Mockito.verify(currencyService, Mockito.times(1)).getCurrenciesPerSingleDay(LocalDate.now(), codes, base);
    }

    @Test
    public void getLatestRatestCallsCurrencyServiceWhenSingleCodeParameterAndBaseArePassed() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("USD");
        String base = "DZD";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("DZD", "Algerian dinar");
        availableCurrencies.put("currencies", currency);
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates(codes.get(0), base);
        Mockito.verify(currencyService, Mockito.times(1)).getCurrenciesPerSingleDay(LocalDate.now(), codes, base);
    }

    @Test
    public void getLatestRatestCallsCurrencyServiceWhenMultipleCodeParametersAndBaseArePassed() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("BGN");
        codes.add("USD");
        String base = "DZD";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("DZD", "Algerian dinar");
        availableCurrencies.put("currencies", currency);
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates("bgn,usd", base);
        Mockito.verify(currencyService, Mockito.times(1)).getCurrenciesPerSingleDay(LocalDate.now(), codes, base);
    }

    @Test(expected = UnprocessableEntityException.class)
    public void getLatestRatestThrowsExceptionWhenBaseDoesNotExist() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("BGN");
        String base = "xxx";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("DZD", "Algerian dinar");
        availableCurrencies.put("currencies", currency);
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates(codes.get(0), base);
    }

    @Test(expected = BadParameterException.class)
    public void getLatestRatestThrowsExceptionWhenBaseIsMalformattedNumberOfLettersMore() {
        String base = "xxxx";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates("all", base);
    }

    @Test(expected = BadParameterException.class)
    public void getLatestRatestThrowsExceptionWhenBaseIsMalformattedNumberOfLettersLess() {
        String base = "xx";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates("all", base);
    }

    @Test(expected = BadParameterException.class)
    public void getLatestRatestThrowsExceptionWhenBaseIsMalformattedNonLetterSymbol() {
        String base = "x1x";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates("all", base);
    }

    @Test(expected = BadParameterException.class)
    public void getLatestRatestThrowsExceptionWhenCodeIsMalformattedNumberOfLettersMore() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("xxxx");
        String base = "eur";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates(codes.get(0), base);
    }

    @Test(expected = BadParameterException.class)
    public void getLatestRatestThrowsExceptionWhenCodeIsMalformattedNumberOfLettersLess() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("xx");
        String base = "eur";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates(codes.get(0), base);
    }

    @Test(expected = BadParameterException.class)
    public void getLatestRatestThrowsExceptionWhenCodeIsMalformattedNonLetterSymbol() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("x1x");
        String base = "eur";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates(codes.get(0), base);
    }

    @Test
    public void getLatestRatestCallsCurrencyServiceWhenCodeDoesNotExist() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("BGN");
        codes.add("USD");
        String base = "DZD";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("DZD", "Algerian dinar");
        currency.put("BGN", "Bulgarian lev");
        availableCurrencies.put("currencies", currency);
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getLatestRates("bgn,usd", base);
        Mockito.verify(currencyService, Mockito.times(1)).getCurrenciesPerSingleDay(LocalDate.now(), codes, base);
    }

    @Test(expected = ResponseStatusException.class)
    public void getLatestRatestThrowsResponseStatusException() {
        Mockito.when(currencyController.getLatestRates("all", "eur")).thenThrow(RuntimeException.class);
        currencyController.getLatestRates("all", "eur");
    }

    @Test
    public void getAvailableCurrenciesCallsCurrencyServiceWhenEverIsFalse() {
        currencyController.getAvailableCurrencies(false);
        Mockito.verify(currencyService, Mockito.times(1)).getAvailableCurrencies(false);
    }

    @Test
    public void getAvailableCurrenciesCallsCurrencyServiceWhenEverIsTrue() {
        currencyController.getAvailableCurrencies(true);
        Mockito.verify(currencyService, Mockito.times(1)).getAvailableCurrencies(true);
    }

    @Test(expected = ResponseStatusException.class)
    public void getAvailableCurrenciesThrowsResponseStatusException() {
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenThrow(RuntimeException.class);
        currencyController.getAvailableCurrencies(false);
    }

    //getCurrenciesPerDate

    @Test(expected = ResponseStatusException.class)
    public void getCurrenciesPerDateThrowsResponseStatusException() {
        Mockito.when(currencyController.getCurrenciesPerDate("2020-05-12", "all", "eur")).thenThrow(RuntimeException.class);
        currencyController.getCurrenciesPerDate("2020-05-12", "all", "eur");
    }

    @Test(expected = BadParameterException.class)
    public void getCurrenciesPerDateThrowsBadParameterExceptionWhenDateFormatIncorrect() {
        currencyController.getCurrenciesPerDate("2020-05-32", "all", "eur");
    }

    @Test(expected = BadParameterException.class)
    public void getCurrenciesPerDateThrowsBadParameterExceptionWhenDateIsAfterToday() {
        LocalDate localDate = LocalDate.now().plusDays(1);
        currencyController.getCurrenciesPerDate(localDate.toString(), "all", "eur");
    }

    @Test(expected = BadParameterException.class)
    public void getCurrenciesPerDateThrowsBadParameterExceptionWhenDateIsBefore1999() {
        LocalDate localDate = LocalDate.of(1998, 12, 31);
        currencyController.getCurrenciesPerDate(localDate.toString(), "all", "eur");
    }

    @Test(expected = BadParameterException.class)
    public void getCurrenciesPerDateThrowsBadParameterExceptionWhenDateCannotBeParsed() {
        currencyController.getCurrenciesPerDate("date", "all", "eur");
    }

    @Test(expected = BadParameterException.class)
    public void getCurrenciesPerDateThrowsBadParameterExceptionWhenBaseLessNumberOfSymbols() {
        currencyController.getCurrenciesPerDate("2020-05-20", "all", "xx");
    }

    @Test(expected = BadParameterException.class)
    public void getCurrenciesPerDateThrowsBadParameterExceptionWhenBaseMoreNumberOfSymbols() {
        currencyController.getCurrenciesPerDate("2020-05-20", "all", "xxxx");
    }

    @Test(expected = BadParameterException.class)
    public void getCurrenciesPerDateThrowsBadParameterExceptionWhenBaseUnvalidSymbols() {
        currencyController.getCurrenciesPerDate("2020-05-20", "all", "xx1");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void getCurrenciesPerDateThrowsExceptionWhenBaseDoesNotExist() {
        ArrayList<String> codes = new ArrayList<>();
        codes.add("BGN");
        String base = "xxx";
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("DZD", "Algerian dinar");
        availableCurrencies.put("currencies", currency);
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getCurrenciesPerDate("2020-05-20", "all", "xxx");
    }

    @Test
    public void getCurrenciesPerDateCallsServiceWhenInputCorrect() {
        String codes = "bgn,usd,czk";
        ArrayList<String> codesResult = new ArrayList<String>();
        codesResult.add("BGN");
        codesResult.add("USD");
        codesResult.add("CZK");
        LocalDate date = LocalDate.of(2020, 5, 20);
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("DZD", "Algerian dinar");
        availableCurrencies.put("currencies", currency);
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getCurrenciesPerDate(date.toString(), codes, "DZD");
        Mockito.verify(currencyService, Mockito.times(1)).getCurrenciesPerSingleDay(date, codesResult, "DZD");
    }

    //getTimeSeries

    @Test(expected = ResponseStatusException.class)
    public void getTimeSeriesThrowsResponseStatusException() {
        Mockito.when(currencyController.getTimeSeries("all", "2018-05-12", "2020-05-12", "1", "15", "eur")).thenThrow(RuntimeException.class);
        currencyController.getTimeSeries("all", "2018-05-12", "2020-05-12", "1", "15", "eur");
    }

    @Test(expected = BadParameterException.class)
    public void getTimeSeriesThrowsBadParameterExceptionWhenDateFormatIncorrectStart() {
        currencyController.getTimeSeries("all", "2018-05-32", "2020-05-12", "1", "15", "eur");
    }

    @Test(expected = BadParameterException.class)
    public void getTimeSeriesThrowsBadParameterExceptionWhenDateFormatIncorrectEnd() {
        currencyController.getTimeSeries("all", "2018-05-12", "2020-05-32", "1", "15", "eur");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void getTimeSeriesThrowsUnprocessableEntityExceptionWhenEndIsBeforeStart() {
        currencyController.getTimeSeries("all", "2020-05-12", "2018-05-12", "1", "15", "eur");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void getTimeSeriesThrowsUnprocessableEntityExceptionWhenPageIsNegative() {
        currencyController.getTimeSeries("all", "2020-05-12", "2018-05-12", "-1", "15", "eur");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void getTimeSeriesThrowsUnprocessableEntityExceptionWhenPageNotNumber() {
        currencyController.getTimeSeries("all", "2020-05-12", "2018-05-12", "x", "15", "eur");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void getTimeSeriesThrowsUnprocessableEntityExceptionWhenLimitNotNumber() {
        currencyController.getTimeSeries("all", "2020-05-12", "2018-05-12", "1", "x", "eur");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void getTimeSeriesThrowsUnprocessableEntityExceptionWhenLimitIsNegative() {
        currencyController.getTimeSeries("all", "2020-05-12", "2018-05-12", "1", "-15", "eur");
    }

    @Test(expected = BadParameterException.class)
    public void getTimeSeriesThrowsBadParameterExceptionWhenBaseLessNumberOfSymbols() {
        currencyController.getTimeSeries("all", "2018-05-12", "2020-05-12", "1", "15", "eu");
    }

    @Test(expected = BadParameterException.class)
    public void getTimeSeriesThrowsBadParameterExceptionWhenBaseMoreNumberOfSymbols() {
        currencyController.getTimeSeries("all", "2018-05-12", "2020-05-12", "1", "15", "euro");
    }

    @Test(expected = BadParameterException.class)
    public void getTimeSeriesThrowsBadParameterExceptionWhenBaseUnvalidSymbols() {
        currencyController.getTimeSeries("all", "2018-05-12", "2020-05-12", "1", "15", "x1x");
    }

    @Test
    public void getTimeSeriesCallsServiceWhenInputCorrect() {
        String codes = "bgn,usd,czk";
        ArrayList<String> codesResult = new ArrayList<String>();
        codesResult.add("BGN");
        codesResult.add("USD");
        codesResult.add("CZK");
        LocalDate start = LocalDate.of(2018, 5, 12);
        LocalDate end = LocalDate.of(2020, 5, 12);
        Map<String, Map<String, String>> availableCurrencies = new TreeMap<>();
        Map<String, String> currency = new HashMap<>();
        currency.put("DZD", "Algerian dinar");
        availableCurrencies.put("currencies", currency);
        Mockito.when(currencyController.getAvailableCurrencies(false)).thenReturn(availableCurrencies);
        currencyController.getTimeSeries("bgn,usd,czk", "2018-05-12", "2020-05-12", "1", "15", "eur");
        Mockito.verify(currencyService, Mockito.times(1)).getTimeSeries(start, end, "1", "15", "eur", codesResult);
    }
}
