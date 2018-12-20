package com.maverick.domain.projection;

import com.maverick.domain.Sale;
import lombok.Value;

import java.util.SortedSet;

@Value
public class SalesProjection {

    private SortedSet<Sale> expectedSales;
}
