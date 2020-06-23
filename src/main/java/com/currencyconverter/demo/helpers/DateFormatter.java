package com.currencyconverter.demo.helpers;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static com.currencyconverter.demo.constants.ApiConstants.*;
import static com.currencyconverter.demo.helpers.ParameterValidityChecker.checkDateParameter;

public class DateFormatter {
    public static String formatDate(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATA_FORMAT_IN_URI_STRING);
        LocalDate localDate = checkDateParameter(date);
        localDate = adjustDateToWeekends(localDate);
        localDate = adjustToTargetClosingDays(localDate);
        return dtf.format(localDate);
    }

    public static HashMap<String, Integer> calculateActiveDaysOfTimeRange(LocalDate from, LocalDate to) {
        int activeDaysCount = 0;
        int totalDaysCount = 0;
        HashMap<String, Integer> days = new HashMap<>();
        while (from.isBefore(to.plusDays(1))) {
            totalDaysCount++;
            if (from.getDayOfWeek() != DayOfWeek.SUNDAY
            && from.getDayOfWeek() != DayOfWeek.SATURDAY) {
                if (!checkIfDayIsTargetClosingDay(from)) {
                    activeDaysCount++;
                }
            }
            from = from.plusDays(1);
        }
        days.put("Total days", totalDaysCount);
        days.put("Active days", activeDaysCount);
        return days;
    }

    public static LocalDate calculateEaster(int year) {
        int a = year % 19,
                b = year / 100,
                c = year % 100,
                d = b / 4,
                e = b % 4,
                g = (8 * b + 13) / 25,
                h = (19 * a + b - d - g + 15) % 30,
                j = c / 4,
                k = c % 4,
                m = (a + 11 * h) / 319,
                r = (2 * e + 2 * j - k - h + m + 32) % 7,
                n = (h - m + r + 90) / 25,
                p = (h - m + r + n + 19) % 32;
        return LocalDate.of(year, n, p);
    }

    private static LocalDate adjustToTargetClosingDays(LocalDate localDate) {
        if (localDate.getMonthValue() == 1 && localDate.getDayOfMonth() == 1) {
            localDate = getDateBeforeTargetClosing(localDate);
        } else if (localDate.getMonthValue() == 12 && (localDate.getDayOfMonth() == 25 || localDate.getDayOfMonth() == 26)) {
            localDate = getDateBeforeTargetClosing(localDate);
        } else if (localDate.getMonthValue() == 5 && localDate.getDayOfMonth() == 1) {
            localDate = getDateBeforeTargetClosing(localDate);
        } else if (localDate.getMonthValue() == 3 || localDate.getMonthValue() == 4) {
            LocalDate easter = calculateEaster(localDate.getYear());
            if (localDate.equals(easter.plus(1, ChronoUnit.DAYS)) || localDate.equals(easter.minus(2, ChronoUnit.DAYS))) {
                localDate = getDateBeforeTargetClosing(localDate);
            }
        } else {
            return localDate;
        }
        return localDate;
    }

    private static boolean checkIfDayIsTargetClosingDay(LocalDate localDate) {
        if (localDate.getMonthValue() == 1 && localDate.getDayOfMonth() == 1) {
            return true;
        } else if (localDate.getMonthValue() == 12 && (localDate.getDayOfMonth() == 25 || localDate.getDayOfMonth() == 26)) {
            return true;
        } else if (localDate.getMonthValue() == 5 && localDate.getDayOfMonth() == 1) {
            return true;
        } else if (localDate.getMonthValue() == 3 || localDate.getMonthValue() == 4) {
            LocalDate easter = calculateEaster(localDate.getYear());
            return localDate.equals(easter.plus(1, ChronoUnit.DAYS)) || localDate.equals(easter.minus(2, ChronoUnit.DAYS));
        } else {
            return false;
        }
    }

    private static LocalDate getDateBeforeTargetClosing(LocalDate localDate) {
        localDate = getPreviousDay(localDate, 1);
        localDate = adjustDateToWeekends(localDate);
        localDate = adjustToTargetClosingDays(localDate);
        return localDate;
    }

    public static String formatTodayDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATA_FORMAT_IN_URI_STRING);
        if (LOCAL_TIME_TO_CET.isAfter(CURRENCY_RATE_UPDATE_TIME)) {
            LocalDateTime todayDate = LocalDateTime.now();
            return dtf.format(todayDate);
        }
        return dtf.format(getPreviousDay());
    }

    private static LocalDate adjustDateToWeekends(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            localDate = getPreviousDay(localDate, 2);
        } else if (dayOfWeek == DayOfWeek.SATURDAY) {
            localDate = getPreviousDay(localDate, 1);
        }
        return localDate;
    }

    private static LocalDateTime getPreviousDay() {
        Instant now = Instant.now();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        return LocalDateTime.ofInstant(yesterday, ZoneOffset.UTC);
    }

    private static LocalDate getPreviousDay(LocalDate localDate, int days) {
        return localDate.minusDays(days);
    }
}
