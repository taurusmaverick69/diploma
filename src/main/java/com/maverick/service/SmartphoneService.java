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
import org.springframework.util.CollectionUtils;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.maverick.domain.document.Smartphone.COUNTER_POINT_MAP;
import static com.maverick.domain.document.Smartphone.YEAR_QUARTER_SMARTPHONE_QUANTITY_MAP;
import static java.time.Month.*;

@Service
public class SmartphoneService {

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


                TreeSet<Sale> sales = yearMonthPercentMap.entrySet().stream().map(yearMonthPercent -> {
                    YearMonth yearMonth = yearMonthPercent.getKey();
                    Float percent = yearMonthPercent.getValue();
                    Sale sale = new Sale();
                    sale.setYearMonth(yearMonth);
                    Integer quantityByYearMonth = yearMonthSumQuantityMap.get(yearMonth);
                    int quantity = Math.round(quantityByYearMonth * (percent / 100f));
                    sale.setQuantity(quantity);
                    return sale;
                }).collect(Collectors.toCollection(TreeSet::new));
                smartphone.setExpectedSales(sales);
                return smartphone;

            }).collect(Collectors.toList());

//            SmartphoneDto[] smartphoneDtos = mapper.readValue(new File("response-data-export.json"), SmartphoneDto[].class);
//            List<Smartphone> smartphones = Arrays.stream(smartphoneDtos).map(smartphoneDto -> {
//
//                String deviceModel = smartphoneDto.getDeviceModel();
//                float popularity = smartphoneDto.getPopularity();
//                String brand = StringUtils.substringBefore(deviceModel, StringUtils.SPACE);
//                String model = StringUtils.substringAfter(deviceModel, StringUtils.SPACE);
//
//                Smartphone smartphone = new Smartphone();
//                smartphone.setBrand(brand);
//                smartphone.setModel(model);
//
//                SortedSet<Sale> expectedSales = yearMonthSumQuantityMap.entrySet().stream().map(yearMonthSumQuantity -> {
//                    Sale sale = new Sale();
//                    sale.setYearMonth(yearMonthSumQuantity.getKey());
//                    int regionQuantity = Math.round(yearMonthSumQuantity.getValue() * (popularity / 100f));
//                    sale.setQuantity(regionQuantity);
//                    return sale;
//                }).collect(Collectors.toCollection(TreeSet::new));
//
//                smartphone.setExpectedSales(expectedSales);
//                return smartphone;
//            }).collect(Collectors.toList());

            smartphoneRepository.deleteAll();
            smartphoneRepository.insert(smartphones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SortedSet<BrandModelProjection> getBrandModels() {
        return smartphoneRepository.findAllBy(BrandModelProjection.class);
    }

    public StatisticProjection getStatisticById(ObjectId objectId) {
        StatisticProjection projection = smartphoneRepository.findByIdIs(objectId);
        SortedSet<Sale> expectedSales = projection.getExpectedSales();
        SortedMap<YearMonth, Float> currentCoefficients = projection.getCoefficients();

        Map<YearMonth, Float> blankCoefficients = expectedSales.stream().map(Sale::getYearMonth)
                .filter(Predicate.not(currentCoefficients::containsKey))
                .collect(Collectors.toMap(o -> o, o -> 1.0f));
        currentCoefficients.putAll(blankCoefficients);



        Sale firstSale = projection.getExpectedSales().first();
        SortedMap<YearMonth, Float> yearMonthCoefficientMap = currentCoefficients.tailMap(firstSale.getYearMonth());

        TreeSet<Sale> calculatedSales = new TreeSet<>();
        calculatedSales.add(firstSale);


        yearMonthCoefficientMap.forEach((yearMonth, coefficient) -> {
            Sale sale = new Sale();
            sale.setYearMonth(yearMonth);
            Sale leftSale = calculatedSales.floor(sale);
            sale.setYearMonth(yearMonth);
            sale.setQuantity(Math.round(leftSale.getQuantity() * coefficient));
            calculatedSales.add(sale);
        });


        SortedSet<Sale> customCoefficientSales = projection.getCalculatedSales();
        customCoefficientSales.clear();
        customCoefficientSales.addAll(calculatedSales);
        return projection;
    }

    public void updateCoefficients(Smartphone smartphone) {
        Query query = Query.query(Criteria.where("_id").is(smartphone.getId()));
        Update update = Update.update("coefficients", smartphone.getCoefficients());
        mongoOperations.updateFirst(query, update, Smartphone.class);
    }
}
