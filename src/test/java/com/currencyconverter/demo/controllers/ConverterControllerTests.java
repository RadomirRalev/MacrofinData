package com.currencyconverter.demo.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConverterControllerTests {
    @InjectMocks
    private ConverterController converterController;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertNotNull(converterController);
    }

    // getCurrencyConverter

}
