package com.maverick.domain.enums;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Optional;

import static java.time.Month.*;
import static java.time.ZoneId.systemDefault;

public enum Season {

    WINTER, SPRING, SUMMER, AUTUMN;

    public static Season of(Month month) {

        if (month == DECEMBER || month == JANUARY || month == FEBRUARY) return WINTER;
        if (month == MARCH || month == APRIL || month == MAY) return SPRING;
        if (month == JUNE || month == JULY || month == AUGUST) return SUMMER;
        if (month == SEPTEMBER || month == OCTOBER || month == NOVEMBER) return AUTUMN;

        throw new IllegalStateException();
    }

    public static Date firstDateOf(Season season) {

        int year = LocalDate.now().getYear();
        Month firstMonth = null;
        if (season == WINTER) firstMonth = DECEMBER;
        if (season == SPRING) firstMonth = MARCH;
        if (season == SUMMER) firstMonth = JUNE;
        if (season == AUTUMN) firstMonth = SEPTEMBER;

        Month month = Optional.ofNullable(firstMonth).orElseThrow(IllegalStateException::new);
        return Date.from(LocalDate.of(year, month, 1).atStartOfDay().atZone(systemDefault()).toInstant());
    }
}
