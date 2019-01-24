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
    Double price = 500.0;
    NavigableMap<YearMonth, Float> ratings;
    NavigableMap<YearMonth, Sale> sales;
    NavigableMap<YearMonth, Float> coefficients;

    public void setCustomCoefficientsSales() {
        Map.Entry<YearMonth, Sale> firstSaleEntry = sales.firstEntry();
        YearMonth firstYearMonth = firstSaleEntry.getKey();
        Sale firstSale = firstSaleEntry.getValue();

        Map.Entry<YearMonth, Float> firstRatingEntry = ratings.firstEntry();
        Float firstRating = firstRatingEntry.getValue();

        firstSale.setCustomCoefficientDisabledQuantity(Math.round(firstSale.getExpectedQuantity()));
        firstSale.setCustomCoefficientRatingEnabledQuantity(Math.round(firstSale.getExpectedQuantity()));
        firstSale.setCustomCoefficientSeasonEnabledOnlyQuantity(Math.round(firstSale.getExpectedQuantity()));
        firstSale.setCustomCoefficientEnabledQuantity(Math.round(firstSale.getExpectedQuantity()));

        SortedMap<YearMonth, Float> yearMonthCoefficientMap = coefficients.tailMap(firstYearMonth, false);
        yearMonthCoefficientMap.forEach((yearMonth, coefficient) -> {
            Sale leftSale = sales.lowerEntry(yearMonth).getValue();
            Sale currentSale = sales.get(yearMonth);
            float rating = ratings.get(yearMonth);


            currentSale.setCustomCoefficientDisabledQuantity(Math.round(leftSale.getCustomCoefficientDisabledQuantity() * DEFAULT_SEASON_COEFFICIENT * DEFAULT_RATING * RATING_MULTIPLIER));
            currentSale.setCustomCoefficientRatingEnabledQuantity(Math.round(leftSale.getCustomCoefficientRatingEnabledQuantity() * DEFAULT_SEASON_COEFFICIENT * rating * RATING_MULTIPLIER));
            currentSale.setCustomCoefficientSeasonEnabledOnlyQuantity(Math.round(leftSale.getCustomCoefficientSeasonEnabledOnlyQuantity() * coefficient * DEFAULT_RATING * RATING_MULTIPLIER));
            currentSale.setCustomCoefficientEnabledQuantity(Math.round(leftSale.getCustomCoefficientEnabledQuantity() * coefficient * rating * RATING_MULTIPLIER));
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

    public Map<String, SortedMap<YearQuarter, Map<String, Pair<? extends Number, ? extends Number>>>> getMethodEvaluation() {

        Map<YearQuarter, List<Sale>> yearQuarterSalesMap = sales.entrySet().stream().collect(
                Collectors.groupingBy(o -> YearQuarter.from(o.getKey()),
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        SortedMap<YearQuarter, Map<String, Pair<? extends Number, ? extends Number>>> numberResult = new TreeMap<>();
        SortedMap<YearQuarter, Map<String, Pair<? extends Number, ? extends Number>>> moneyResult = new TreeMap<>();

        yearQuarterSalesMap.forEach((yearQuarter, sales) -> {
            Map<String, List<Integer>> typeDiffsPairMap = new HashMap<>();
            List<Integer> movingAverageDiffs = new ArrayList<>();
            List<Integer> disabledDiffs = new ArrayList<>();
            List<Integer> RatingOnlyDiffs = new ArrayList<>();
            List<Integer> SeasonsOnlyDiffs = new ArrayList<>();
            List<Integer> RatingAndSeasonsDiffs = new ArrayList<>();

            sales.forEach(sale -> {
                movingAverageDiffs.add(sale.getMovingAverageQuantity() - sale.getExpectedQuantity());
                disabledDiffs.add(sale.getCustomCoefficientDisabledQuantity() - sale.getExpectedQuantity());
                RatingOnlyDiffs.add(sale.getCustomCoefficientRatingEnabledQuantity() - sale.getExpectedQuantity());
                SeasonsOnlyDiffs.add(sale.getCustomCoefficientSeasonEnabledOnlyQuantity() - sale.getExpectedQuantity());
                RatingAndSeasonsDiffs.add(sale.getCustomCoefficientEnabledQuantity() - sale.getExpectedQuantity());
            });

            typeDiffsPairMap.put("Moving average", movingAverageDiffs);
            typeDiffsPairMap.put("Disabled", disabledDiffs);
            typeDiffsPairMap.put("RatingOnly", RatingOnlyDiffs);
            typeDiffsPairMap.put("SeasonsOnly", SeasonsOnlyDiffs);
            typeDiffsPairMap.put("Rating And Seasons", RatingAndSeasonsDiffs);

            Map<String, Pair<? extends Number, ? extends Number>> typeNumberPairMap = new HashMap<>();
            Map<String, Pair<? extends Number, ? extends Number>> typeMoneyPairMap = new HashMap<>();

            typeDiffsPairMap.forEach((s, diffs) -> {
                Pair<Integer, Integer> countPair = getCouldRedundantSoldPair(diffs);
                typeNumberPairMap.put(s, countPair);

                Pair<Double, Double> moneyPair = Pair.of(countPair.getFirst()  * 0.02 * price, countPair.getSecond() * 4.0);
                typeMoneyPairMap.put(s, moneyPair);
            });

            numberResult.put(yearQuarter, typeNumberPairMap);
            moneyResult.put(yearQuarter, typeMoneyPairMap);
        });

        return Map.of("Number", numberResult, "Money", moneyResult);
    }

    private Pair<Integer, Integer> getCouldRedundantSoldPair(List<Integer> differences) {
        int couldSold = 0;
        int redundantPurchase = 0;

        for (Integer difference : differences) {
            if (difference < 0) {
                couldSold += difference;
            } else {
                redundantPurchase += difference;
            }
        }
        return Pair.of(couldSold, redundantPurchase);
    }
}


