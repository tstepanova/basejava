package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate of(String localDate, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        YearMonth ym = YearMonth.parse(localDate, dtf);
        LocalDate ld = ym.atDay(1);
        return ld;
    }
}