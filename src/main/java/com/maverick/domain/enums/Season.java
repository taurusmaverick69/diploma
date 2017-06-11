package com.maverick.domain.enums;

import java.time.Month;

import static java.time.Month.*;

public enum Season {

    WINTER, SPRING, SUMMER, AUTUMN;

    public static Season of(Month month) {

        if (month == DECEMBER || month == JANUARY || month == FEBRUARY) return WINTER;
        if (month == MARCH || month == APRIL || month == MAY) return SPRING;
        if (month == JUNE || month == JULY || month == AUGUST) return SUMMER;
        if (month == SEPTEMBER || month == OCTOBER || month == NOVEMBER) return AUTUMN;

        throw new IllegalStateException();
    }
}
