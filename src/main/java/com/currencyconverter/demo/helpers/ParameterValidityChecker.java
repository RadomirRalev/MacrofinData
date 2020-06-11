package com.currencyconverter.demo.helpers;

import com.currencyconverter.demo.exceptions.BadParameterException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.currencyconverter.demo.constants.ExceptionConstants.*;

public class ParameterValidityChecker {
    public static int processIdPathVariable(String id) {
        int idNumber;
        try {
            idNumber = Integer.parseInt(id);
        } catch (NumberFormatException nfe) {
            throw new BadParameterException(BAD_PARAMETER_ID_NOT_NUMBER);
        }
        if (idNumber < 0) {
            throw new BadParameterException(BAD_PARAMETER_ID_LESS_THAN_ZERO);
        }
        return idNumber;
    }

    public static void checkIfCodePathVariableIsCorrect(@PathVariable String code) {
        if (code.length() != 3) {
            throw new BadParameterException(BAD_PARAMETER_ISO_INCORRECT_NUMBER_LETTERS);
        }
        for (int i = 0; i < code.length(); i++) {
            if ((code.charAt(i) < 65 || code.charAt(i) > 122) || (code.charAt(i) < 96 && code.charAt(i) > 90)) {
                throw new BadParameterException(BAD_PARAMETER_ISO_NO_LETTERS);
            }
        }
    }

    public static void checkIfAmountIsCorrect(@RequestParam String amount) {
        for (int i = 0; i < amount.length(); i++) {
            if ((amount.charAt(i) < 48 || amount.charAt(i) > 57) && amount.charAt(i) != 46) {
                throw new BadParameterException(AMOUNT_NO_INTEGER);
            }
        }
    }
}
