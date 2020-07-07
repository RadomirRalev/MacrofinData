package com.currencyconverter.demo.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ConverterControllerTests {
    @InjectMocks
    private ConverterController converterController;

    @Test
    public void contextLoads() throws Exception {
        Assert.assertNotNull(converterController);
    }

    // getCurrencyConverter

//    @Test(expected = ResponseStatusException.class)
//    public void getCurrencyConverterThrowsResponseStatusException() {
//        Mockito.when(converterController.getCurrencyConverter("eur", "eur", "2020-02-05", "10.54")).thenThrow(RuntimeException.class);
//        converterController.getCurrencyConverter("eur", "eur", "2020-02-05", "10.54");
//    }
}
