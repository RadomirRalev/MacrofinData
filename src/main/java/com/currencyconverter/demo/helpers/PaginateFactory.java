package com.currencyconverter.demo.helpers;

import com.currencyconverter.demo.exceptions.client.BadParameterException;
import com.currencyconverter.demo.exceptions.client.UnprocessableEntityException;

import java.time.LocalDate;
import java.util.HashMap;

import static com.currencyconverter.demo.constants.ControllerConstants.*;
import static com.currencyconverter.demo.constants.ControllerConstants.END_DATE;
import static com.currencyconverter.demo.constants.ExceptionConstants.INVALID_PAGE_NUMBER_HIGHER_THAN_SELECTION;
import static com.currencyconverter.demo.helpers.DateFormatter.calculateActiveDaysOfTimeRange;

public class PaginateFactory {

    public static HashMap<String, LocalDate> paginateTimeSeries(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit) {
        HashMap<String, LocalDate> mapOfLocalDatesPaginated = new HashMap<>();
        if (page.equalsIgnoreCase(LAST_PAGE)) {
            page = getLastPage(localDateFrom, localDateTo, limit);
        }
        localDateFrom = getLocalDatePaginated(localDateFrom, localDateTo, page, limit);
        mapOfLocalDatesPaginated.put(START_DATE, localDateFrom);
        LocalDate localDateToPaginated = localDateFrom.plusDays(Integer.parseInt(limit) - 1);
        if (localDateToPaginated.isAfter(localDateTo)) {
            mapOfLocalDatesPaginated.put(END_DATE, localDateTo);
        } else {
            mapOfLocalDatesPaginated.put(END_DATE, localDateToPaginated);
        }
        return mapOfLocalDatesPaginated;
    }

    private static String getLastPage(LocalDate localDateFrom, LocalDate localDateTo, String limit) {
        String page;
        int totalNumberOfDaysInTimeSeries = calculateActiveDaysOfTimeRange(localDateFrom, localDateTo).get("Total days");
        int pageInt = (totalNumberOfDaysInTimeSeries + Integer.parseInt(limit) - 1) / Integer.parseInt(limit);
        page = Integer.toString(pageInt);
        return page;
    }

    private static LocalDate getLocalDatePaginated(LocalDate localDateFrom, LocalDate localDateTo, String page, String limit) {
        LocalDate localDateFromPaginated = localDateFrom.plusDays((Integer.parseInt(page) - 1) * Integer.parseInt(limit));
        if (localDateFromPaginated.isAfter(localDateTo)) {
            throw new UnprocessableEntityException(INVALID_PAGE_NUMBER_HIGHER_THAN_SELECTION);
        } else {
            localDateFrom = localDateFromPaginated;
        }
        return localDateFrom;
    }
}
