package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.Organization;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static ru.javawebinar.basejava.model.Organization.Position.PATTERN;

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

    public static LocalDate of(String localDate) {
        if(localDate == null || localDate.isEmpty()) {
            return NOW;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN);
        YearMonth ym = YearMonth.parse(localDate, dtf);
        LocalDate ld = ym.atDay(1);
        return ld;
    }

    public static String of(LocalDate localDate) {
        if(localDate.isEqual(NOW)) return "";
        return DateTimeFormatter.ofPattern(PATTERN).format(localDate);
    }

    public static String of(Organization.Position position) {
        return DateTimeFormatter.ofPattern(PATTERN).format(position.getStartDate()) + " - " + (position.getEndDate().isEqual(NOW) ? "Сейчас" : DateTimeFormatter.ofPattern(PATTERN).format(position.getEndDate()));
    }
}