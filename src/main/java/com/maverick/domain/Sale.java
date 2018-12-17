package com.maverick.domain;

import lombok.Data;
import org.threeten.extra.YearQuarter;

import java.time.LocalDate;

@Data
public class Sale implements Comparable<Sale> {

        private LocalDate date;
        private YearQuarter yearQuarter;
        private int quantity;

        @Override
        public int compareTo(Sale o) {
            return yearQuarter.compareTo(o.getYearQuarter());
        }
}
