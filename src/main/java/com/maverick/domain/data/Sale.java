package com.maverick.domain.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.YearMonth;

@Data
@EqualsAndHashCode(of = "yearMonth")
public class Sale implements Comparable<Sale> {

    private YearMonth yearMonth;
    private int quantity;

    @Override
    public int compareTo(Sale o) {
        return yearMonth.compareTo(o.getYearMonth());
    }
}
