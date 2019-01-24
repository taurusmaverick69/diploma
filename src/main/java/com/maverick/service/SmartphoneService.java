package com.maverick.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maverick.domain.data.Sale;
import com.maverick.domain.document.Smartphone;
import com.maverick.domain.projection.BasicProjection;
import com.maverick.domain.projection.BrandModelProjection;
import com.maverick.domain.projection.StatisticProjection;
import com.maverick.repository.SmartphoneRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.threeten.extra.LocalDateRange;
import org.threeten.extra.YearQuarter;

import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.maverick.domain.document.Smartphone.COUNTER_POINT_MAP;
import static com.maverick.domain.document.Smartphone.YEAR_QUARTER_SMARTPHONE_QUANTITY_MAP;
import static java.time.Month.*;

@Service
public class SmartphoneService {

    public static final float DEFAULT_SEASON_COEFFICIENT = 1.0f;
    public static final float DEFAULT_RATING = 4.0f;

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ObjectMapper mapper;

    public SortedSet<BasicProjection> findAll() {
        return smartphoneRepository.findAllBy(BasicProjection.class);
    }

    public Optional<Smartphone> findById(ObjectId id) {
        return smartphoneRepository.findById(id);
    }

    public void updateTracking(ObjectId id, Boolean isTracked) {
        smartphoneRepository.findById(id).ifPresent(s -> {
            s.setIsTracked(isTracked);
            smartphoneRepository.save(s);
        });

    }

    public List<Integer> findYears(ObjectId id) {
        return IntStream.rangeClosed(smartphoneRepository.findById(id).get().getReleaseYear(), Year.now().getValue()).boxed().collect(Collectors.toList());
    }

    public Boolean isTimeToDelivery() {
        List<Month> lastMonthsOfSeason = Arrays.asList(FEBRUARY, MAY, AUGUST, NOVEMBER);
        LocalDate currentLocalDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Month currentMonth = currentLocalDate.getMonth();
        int currentDayOfMonth = currentLocalDate.getDayOfMonth();
        return lastMonthsOfSeason.contains(currentMonth) && currentMonth.maxLength() == currentDayOfMonth;
    }

    public SortedSet<BrandModelProjection> getBrandModels() {
        return smartphoneRepository.findAllBy(BrandModelProjection.class);
    }

    public StatisticProjection getCoefficientStatisticById(ObjectId objectId, boolean isRatingEnabled, boolean isSeasonEnabled) {
        StatisticProjection projection = smartphoneRepository.findByIdIs(objectId);
        projection.setCustomCoefficientsSales();
        projection.setMovingAverageSales();
        return projection;
    }

    public void update(Smartphone smartphone) {
        Query query = Query.query(Criteria.where("_id").is(smartphone.getId()));
        Update update = new Update()
                .set("coefficients", smartphone.getCoefficients())
                .set("ratings", smartphone.getRatings());
        mongoOperations.updateFirst(query, update, Smartphone.class);
    }

    public void resetData() {

        int uaPercent = 1;
        int uaPercentage = 100 / uaPercent;

        List<List<String>> dataList = new ArrayList<>();
        try (ICsvListReader listReader = new CsvListReader(new FileReader("response-data-export.csv"), CsvPreference.STANDARD_PREFERENCE)) {
            String[] headers = listReader.getHeader(true);
            List<String> salesList;
            while ((salesList = listReader.read()) != null) {
                dataList.add(salesList);
            }
            Map<YearMonth, Map<String, Float>> yearMonthSmartphoneShare = dataList.stream().collect(Collectors.toMap(
                    data -> YearMonth.parse(data.get(0)),
                    data -> data.stream().skip(1).collect(Collectors.toMap(
                            o -> headers[data.indexOf(o)],
                            Float::valueOf))));

            Map<YearQuarter, Optional<Map<String, Float>>> yearQuarterSumMap = yearMonthSmartphoneShare.entrySet().stream().collect(
                    Collectors.groupingBy(o -> YearQuarter.from(o.getKey()),
                            Collectors.mapping(Map.Entry::getValue,
                                    Collectors.reducing((m1, m2) ->
                                            Stream.of(m1, m2)
                                                    .map(Map::entrySet)
                                                    .flatMap(Collection::stream)
                                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Float::sum)))
                            )));

            Map<YearMonth, Map<String, Integer>> yearMonthSmartphoneQuantityMap = YEAR_QUARTER_SMARTPHONE_QUANTITY_MAP.entrySet().stream().flatMap(pair -> {

                YearQuarter yearQuarter = pair.getKey();
                Map<String, Integer> smartphoneSalesMapByYearQuarter = pair.getValue();

                List<YearMonth> yearMonthsByQuarter = LocalDateRange.of(yearQuarter.atDay(1), yearQuarter.atEndOfQuarter())
                        .stream().map(YearMonth::from)
                        .distinct()
                        .collect(Collectors.toList());

                return yearMonthsByQuarter.stream().map(yearMonth -> {
                    Map<String, Float> smartphoneShareByYearMonth = yearMonthSmartphoneShare.get(yearMonth);
                    Map<String, Integer> smartphoneQuantityByYearMonth = smartphoneSalesMapByYearQuarter.entrySet().stream().collect(Collectors.toMap(
                            Map.Entry::getKey,
                            o -> {
                                String smartphone = o.getKey();
                                Integer quarterSum = o.getValue();
                                Map<String, Float> smartphoneSumMarketShareByYearQuarter = yearQuarterSumMap.get(yearQuarter).orElseThrow(IllegalStateException::new);
                                Float percentSum = smartphoneSumMarketShareByYearQuarter.get(smartphone);
                                Float percentByYearMonth = smartphoneShareByYearMonth.get(smartphone);
                                return Math.round(percentByYearMonth * quarterSum / percentSum);
                            }));
                    return new SimpleEntry<>(yearMonth, smartphoneQuantityByYearMonth);
                });
            }).collect(Collectors.toMap(SimpleEntry::getKey, Map.Entry::getValue));


            Map<YearMonth, Integer> yearMonthSumQuantityMap = yearMonthSmartphoneQuantityMap.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    brandQuantity -> brandQuantity.getValue().values().stream().map(quantity -> quantity / uaPercentage).mapToInt(value -> value).sum()));


            Map<String, Map<YearMonth, Float>> smartphoneYearMonthPercentMap = new HashMap<>();
            COUNTER_POINT_MAP.forEach((yearMonth, smartphonePercentMap) -> {
                smartphonePercentMap.forEach((smartphone, percent) -> {
                    smartphoneYearMonthPercentMap.putIfAbsent(smartphone, new HashMap<>(Map.of(yearMonth, percent)));
                    smartphoneYearMonthPercentMap.computeIfPresent(smartphone, (s, yearMonthPercentMap) -> {
                        yearMonthPercentMap.put(yearMonth, percent);
                        return yearMonthPercentMap;
                    });
                });
            });

            List<Smartphone> smartphones = smartphoneYearMonthPercentMap.entrySet().stream().map(smartphoneYearMonthPercent -> {

                String smartphoneModelBrand = smartphoneYearMonthPercent.getKey();
                Map<YearMonth, Float> yearMonthPercentMap = smartphoneYearMonthPercent.getValue();

                String brand = StringUtils.substringBefore(smartphoneModelBrand, StringUtils.SPACE);
                String model = StringUtils.substringAfter(smartphoneModelBrand, StringUtils.SPACE);

                Smartphone smartphone = new Smartphone();
                smartphone.setBrand(brand);
                smartphone.setModel(model);

                NavigableMap<YearMonth, Sale> sales = new TreeMap<>();
                NavigableMap<YearMonth, Float> coefficients = new TreeMap<>();
                NavigableMap<YearMonth, Float> ratings = new TreeMap<>();

                yearMonthPercentMap.forEach((yearMonth, percent) -> {
                    Sale sale = new Sale();
                    Integer quantityByYearMonth = yearMonthSumQuantityMap.get(yearMonth);
                    int quantity = Math.round(quantityByYearMonth * (percent / 100f));
                    sale.setExpectedQuantity(quantity);
                    sales.put(yearMonth, sale);
                    coefficients.put(yearMonth, DEFAULT_SEASON_COEFFICIENT);
                    ratings.put(yearMonth, DEFAULT_RATING);
                });

                smartphone.setSales(sales);
                smartphone.setCoefficients(coefficients);
                smartphone.setRatings(ratings);
                return smartphone;

            }).collect(Collectors.toList());

            smartphoneRepository.deleteAll();
            smartphoneRepository.insert(smartphones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
