package ru.caloriesmanager.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public static final LocalDate MIN_DATE = LocalDate.of(0,1,1);
    public static final LocalDate MAX_DATE = LocalDate.of(2500,1,1);

    public static <T extends Comparable<T>> boolean isBetweenInclusive(T value, T start, T end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    public static LocalDate parseToLocalDate(String date, LocalDate def) {
        return date.isEmpty() ? def : LocalDate.parse(date);
    }
    public static LocalTime parseToLocalTime(String time, LocalTime def) {
        return time.isEmpty() ? def : LocalTime.parse(time);
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}