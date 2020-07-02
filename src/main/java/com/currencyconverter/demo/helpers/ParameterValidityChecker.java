package com.currencyconverter.demo.helpers;

import com.currencyconverter.demo.exceptions.client.BadParameterException;
import com.currencyconverter.demo.exceptions.client.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

import static com.currencyconverter.demo.constants.ExceptionConstants.*;

public class ParameterValidityChecker {
    public static Logger logger = LoggerFactory.getLogger(ParameterValidityChecker.class);

    public static void checkIfCodePathVariableIsCorrect(@PathVariable String code) {
        if (code.length() != 3) {
            logger.warn(BAD_PARAMETER_ISO_INCORRECT_NUMBER_LETTERS + " - " + code);
            throw new BadParameterException(BAD_PARAMETER_ISO_INCORRECT_NUMBER_LETTERS);
        }
        for (int i = 0; i < code.length(); i++) {
            if ((code.charAt(i) < 65 || code.charAt(i) > 122) || (code.charAt(i) < 96 && code.charAt(i) > 90)) {
                logger.warn(BAD_PARAMETER_ISO_NO_LETTERS + " - " + code);
                throw new BadParameterException(BAD_PARAMETER_ISO_NO_LETTERS);
            }
        }
    }

    public static void checkIfAmountIsCorrect(@RequestParam String amount) {
        for (int i = 0; i < amount.length(); i++) {
            if ((amount.charAt(i) < 48 || amount.charAt(i) > 57) && amount.charAt(i) != 46) {
                logger.warn(AMOUNT_NO_INTEGER + " - " + amount);
                throw new BadParameterException(AMOUNT_NO_INTEGER);
            }
        }
    }

    public static LocalDate checkDateParameter(String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            logger.warn(BAD_PARAMETER_WRONG_DATE_FORMAT + " - " + date);
            throw new UnprocessableEntityException(BAD_PARAMETER_WRONG_DATE_FORMAT, date);
        }
        if (localDate.isAfter(LocalDate.now())) {
            logger.warn(BAD_PARAMETER_WRONG_DATE_FORMAT + " - " + date);
            throw new UnprocessableEntityException(INVALID_DATE_AFTER, date);
        } else if (localDate.isBefore(LocalDate.of(1999, 1, 1))) {
            logger.warn(BAD_PARAMETER_WRONG_DATE_FORMAT + " - " + date);
            throw new UnprocessableEntityException(INVALID_DATE_BEFORE, date);
        }
        return localDate;
    }

    public static void checkIfFromDateIsBeforeToDate(LocalDate localDateFrom, LocalDate localDateTo) {
        if (localDateFrom.isAfter(localDateTo)) {
            logger.warn(INVALID_DATE_FROM_DATE_AFTER_TO_DATE + " - " + localDateFrom.toString() + " is after " + localDateTo.toString());
            throw new UnprocessableEntityException(INVALID_DATE_FROM_DATE_AFTER_TO_DATE, localDateFrom.toString());
        }
    }

    public static String checkIfLimitIsCorrect(String limit) {
        if (limit == null) {
            return "100";
        } else {
            try {
                int limitNum = Integer.parseInt(limit);
                if (limitNum > 100 || limitNum < 1) {
                    logger.warn(INVALID_LIMIT_NUMBER + " - " + limitNum);
                    throw new UnprocessableEntityException(INVALID_LIMIT_NUMBER, limit);
                }
                return limit;
            } catch (Exception e) {
                logger.warn(INVALID_LIMIT_NUMBER + " - " + limit);
                throw new UnprocessableEntityException(INVALID_LIMIT_NUMBER, limit);
            }
        }
    }

    public static String checkIfPageIsCorrect(String page) {
        if (page == null) {
            return "1";
        } else if (page.equalsIgnoreCase("last")) {
            return page;
        } else {
            try {
                int pageNum = Integer.parseInt(page);
                if (pageNum < 1) {
                    logger.warn(INVALID_PAGE_LIMIT_NUMBER + " - " + pageNum);
                    throw new UnprocessableEntityException(INVALID_PAGE_LIMIT_NUMBER, page);
                }
                return page;
            } catch (Exception e) {
                logger.warn(INVALID_PAGE_LIMIT_NUMBER + " - " + page);
                throw new UnprocessableEntityException(INVALID_PAGE_LIMIT_NUMBER, page);
            }
        }
    }

    public static void checkIfCurrencyIsCorrect(String base, Map<String, Map<String, String>> availableCurrencies) {
        Map<String, String> currencyList = availableCurrencies.get("currencies");
        if (!base.equalsIgnoreCase("eur") && !currencyList.containsKey(base.toUpperCase())) {
            ParameterValidityChecker.logger.warn(NO_SUCH_CODE_EXISTS_IN_THE_LIST_OF_ECB_CURRENCIES + " " + base);
            throw new UnprocessableEntityException(NO_SUCH_CODE_EXISTS_IN_THE_LIST_OF_ECB_CURRENCIES, base);
        }
    }
}
