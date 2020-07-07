package com.currencyconverter.demo.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.currencyconverter.demo.constants.ApiConstants.NUMBERS_AFTER_DECIMAL_POINT;

public class MoneyFormatter {

    public static BigDecimal formatSum(double result) {
        BigDecimal bd = BigDecimal.valueOf(result);
        bd = bd.setScale(NUMBERS_AFTER_DECIMAL_POINT, RoundingMode.HALF_UP);
        return bd;
    }
}
