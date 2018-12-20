package com.maverick.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class Sale implements Comparable<Sale> {

    private YearMonth yearMonth;
    private int quantity;

    @Override
    public int compareTo(Sale o) {
        return yearMonth.compareTo(o.getYearMonth());
    }
}
