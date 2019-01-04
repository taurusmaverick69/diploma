package com.maverick.domain.projection;

import com.maverick.domain.data.Sale;
import lombok.Value;
import org.bson.types.ObjectId;

import java.time.YearMonth;
import java.util.*;

@Value
public class StatisticProjection {

    ObjectId id;
    String brand;
    String model;
    private SortedSet<Sale> expectedSales;
    private SortedSet<Sale> calculatedSales = new TreeSet<>();
    private SortedMap<YearMonth, Float> coefficients;
}
