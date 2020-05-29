package com.currencyconverter.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrencyConverterStart {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterStart.class, args);
    }

}
