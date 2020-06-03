package com.currencyconverter.demo.controllers;

import com.currencyconverter.demo.exceptions.BadParameterException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class parameterValidityChecker {
    public static int processIdPathVariable(String id) {
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

    public static void checkIfCodePathVariableIsCorrect(@PathVariable String code) {
        if (code.length() != 3) {
            throw new BadParameterException("ISO code must contain exactly three letters");
        }
        for (int i = 0; i < code.length(); i++) {
            if ((code.charAt(i) < 65 || code.charAt(i) > 122) || (code.charAt(i) < 96 && code.charAt(i) > 90)) {
                throw new BadParameterException("ISO code must contain only letters");
            }
        }
    }

    public static void checkIfAmountIsCorrect(@RequestParam String amount) {
        for (int i = 0; i < amount.length(); i++) {
            if ((amount.charAt(i) < 48 || amount.charAt(i) > 57) && amount.charAt(i) != 46) {
                throw new BadParameterException("Amount must be an integer or decimal number");
            }
        }
    }
}
