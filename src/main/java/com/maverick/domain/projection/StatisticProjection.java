package com.maverick.domain.projection;

import com.maverick.domain.data.Sale;
import lombok.Value;
import org.bson.types.ObjectId;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;

@Value
public class StatisticProjection {

    static float RATING_MULTIPLIER = 0.25f;

    ObjectId id;
    String brand;
    String model;
    float rating;
    NavigableMap<YearMonth, Sale> sales;
    NavigableMap<YearMonth, Float> coefficients;

    public void setCustomCoefficientsSales() {
        Map.Entry<YearMonth, Sale> firstEntry = sales.firstEntry();

        YearMonth firstYearMonth = firstEntry.getKey();
        Sale firstSale = firstEntry.getValue();
        firstSale.setCustomCoefficientQuantity(Math.round(firstSale.getExpectedQuantity() * rating * RATING_MULTIPLIER));

        SortedMap<YearMonth, Float> yearMonthCoefficientMap = coefficients.tailMap(firstYearMonth, false);
        yearMonthCoefficientMap.forEach((yearMonth, coefficient) -> {
            Sale leftSale = sales.lowerEntry(yearMonth).getValue();
            Sale currentSale = sales.get(yearMonth);
            currentSale.setCustomCoefficientQuantity(Math.round(leftSale.getCustomCoefficientQuantity() * coefficient * rating * RATING_MULTIPLIER));
        });
    }

    public void setMovingAverageSales() {
        List<Sale> sales = List.copyOf(this.sales.values());

        for (int i = 0; i < 3; i++) {
            Sale sale = sales.get(i);
            sale.setMovingAverageQuantity(sale.getExpectedQuantity());
        }
        for (int i = 3; i < sales.size(); i++) {
            double avg = List.of(sales.get(i - 1), sales.get(i - 2), sales.get(i - 3)).stream()
                    .mapToInt(Sale::getMovingAverageQuantity)
                    .average()
                    .orElseThrow(IllegalStateException::new);
            sales.get(i).setMovingAverageQuantity(Math.toIntExact(Math.round(avg)));
        }
    }
}
