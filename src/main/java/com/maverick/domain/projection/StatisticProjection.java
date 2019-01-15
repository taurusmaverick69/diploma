package com.maverick.domain.projection;

import com.maverick.domain.data.Sale;
import lombok.Value;
import org.bson.types.ObjectId;
import org.springframework.data.util.Pair;
import org.threeten.extra.YearQuarter;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.maverick.service.SmartphoneService.DEFAULT_RATING;
import static com.maverick.service.SmartphoneService.DEFAULT_SEASON_COEFFICIENT;

@Value
public class StatisticProjection {

    static float RATING_MULTIPLIER = 0.25f;

    ObjectId id;
    String brand;
    String model;
    NavigableMap<YearMonth, Float> ratings;
    NavigableMap<YearMonth, Sale> sales;
    NavigableMap<YearMonth, Float> coefficients;

    public void setCustomCoefficientsSales(boolean isRatingEnabled, boolean isSeasonEnabled) {
        Map.Entry<YearMonth, Sale> firstSaleEntry = sales.firstEntry();
        YearMonth firstYearMonth = firstSaleEntry.getKey();
        Sale firstSale = firstSaleEntry.getValue();

        Map.Entry<YearMonth, Float> firstRatingEntry = ratings.firstEntry();
        Float firstRating = firstRatingEntry.getValue();

        firstSale.setCustomCoefficientQuantity(Math.round(firstSale.getExpectedQuantity()));

        SortedMap<YearMonth, Float> yearMonthCoefficientMap = coefficients.tailMap(firstYearMonth, false);
        yearMonthCoefficientMap.forEach((yearMonth, coefficient) -> {
            Sale leftSale = sales.lowerEntry(yearMonth).getValue();
            Sale currentSale = sales.get(yearMonth);
            float rating = ratings.get(yearMonth);

            rating = isRatingEnabled ? rating : DEFAULT_RATING;
            coefficient = isSeasonEnabled ? coefficient : DEFAULT_SEASON_COEFFICIENT;
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

    public Map<YearQuarter, Pair<Integer, Integer>> getMovingAverageMethodEvaluation() {
        return sales.entrySet().stream().collect(Collectors.groupingBy(o -> YearQuarter.from(o.getKey()),
                Collectors.mapping(yearMonthSaleEntry -> {
                    Sale sale = yearMonthSaleEntry.getValue();
                    return sale.getMovingAverageQuantity() - sale.getExpectedQuantity();
                }, Collectors.toList())))

                .entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        o -> getCouldRedundantSoldPair(o.getValue()), (v1, v2) -> {
                            throw new RuntimeException(String.format("Duplicate key for values %s and %s", v1, v2));
                        }, TreeMap::new
                ));
    }

    public Map<YearQuarter, Pair<Integer, Integer>> getCustomCoefficientsMethodEvaluation() {
        return sales.entrySet().stream().collect(Collectors.groupingBy(o -> YearQuarter.from(o.getKey()),
                Collectors.mapping(yearMonthSaleEntry -> {
                    Sale sale = yearMonthSaleEntry.getValue();
                    return sale.getCustomCoefficientQuantity() - sale.getExpectedQuantity();
                }, Collectors.toList())))

                .entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        o -> getCouldRedundantSoldPair(o.getValue()), (v1, v2) -> {
                            throw new RuntimeException(String.format("Duplicate key for values %s and %s", v1, v2));
                        }, TreeMap::new
                ));
    }

    private Pair<Integer, Integer> getCouldRedundantSoldPair(List<Integer> differences) {
        int couldSold = 0;
        int redundantSold = 0;

        for (Integer difference : differences) {
            if (difference < 0) {
                int compensated = difference + redundantSold;
                if (compensated > 0) {
                    redundantSold += difference;
                } else {
                    redundantSold = 0;
                    couldSold += compensated;
                }
            } else {
                redundantSold += difference;
            }
        }
        return Pair.of(couldSold, redundantSold);
    }
}
