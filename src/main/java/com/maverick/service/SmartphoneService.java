package com.maverick.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maverick.domain.BrandProjection;
import com.maverick.domain.Sale;
import com.maverick.domain.Smartphone;
import com.maverick.domain.SmartphoneDto;
import com.maverick.repository.SmartphoneRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.threeten.extra.LocalDateRange;
import org.threeten.extra.YearQuarter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.Month.*;

@Service
public class SmartphoneService {

    private static final Map<YearQuarter, Map<String, Integer>> YEAR_QUARTER_SMARTPHONE_QUANTITY_MAP = Map.of(
            YearQuarter.of(2017, 1),
            Map.of(
                    "Samsung", 786714000,
                    "Apple", 519925000,
                    "Xiaomi", 127073000
            ),
            YearQuarter.of(2017, 2),
            Map.of(
                    "Samsung", 825351000,
                    "Apple", 443148000,
                    "Xiaomi", 241785000
            ),
            YearQuarter.of(2017, 3),
            Map.of(
                    "Samsung", 856053000,
                    "Apple", 454419000,
                    "Xiaomi", 268532000
            ),
            YearQuarter.of(2017, 4),
            Map.of(
                    "Samsung", 740266000,
                    "Apple", 731752000,
                    "Xiaomi", 281878000
            ),
            YearQuarter.of(2018, 1),
            Map.of(
                    "Samsung", 785648000,
                    "Apple", 540589000,
                    "Xiaomi", 284982000
            ),
            YearQuarter.of(2018, 2),
            Map.of(
                    "Samsung", 723364000,
                    "Apple", 447151000,
                    "Xiaomi", 328255000
            ),
            YearQuarter.of(2018, 3),
            Map.of(
                    "Samsung", 733601000,
                    "Apple", 457466000,
                    "Xiaomi", 332197000
            )
    );
    @Autowired
    private SmartphoneRepository smartphoneRepository;

    @Autowired
    private ObjectMapper mapper;

    public List<Smartphone> findAll() {
        return smartphoneRepository.findAll();
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

    public void resetData() throws IOException {

        int uaPercent = 20;
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


            Map<YearMonth, Integer> yearQuarterSumQuantityMap = yearMonthSmartphoneQuantityMap.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    brandQuantity -> brandQuantity.getValue().values().stream().map(quantity -> quantity / uaPercentage).mapToInt(value -> value).sum()));

            SmartphoneDto[] smartphoneDtos = mapper.readValue(new File("response-data-export.json"), SmartphoneDto[].class);
            List<Smartphone> smartphones = Arrays.stream(smartphoneDtos).map(smartphoneDto -> {

                String deviceModel = smartphoneDto.getDeviceModel();
                float popularity = smartphoneDto.getPopularity();
                String brand = StringUtils.substringBefore(deviceModel, StringUtils.SPACE);
                String model = StringUtils.substringAfter(deviceModel, StringUtils.SPACE);

                Smartphone smartphone = new Smartphone();
                smartphone.setBrand(brand);
                smartphone.setModel(model);

                SortedSet<Sale> expectedSales = yearQuarterSumQuantityMap.entrySet().stream().map(yearMonthSumQuantity -> {
                    Sale sale = new Sale();
                    sale.setYearMonth(yearMonthSumQuantity.getKey());
                    int regionQuantity = Math.round(yearMonthSumQuantity.getValue() * (popularity / 100f));
                    sale.setQuantity(regionQuantity);
                    return sale;
                }).collect(Collectors.toCollection(TreeSet::new));

                smartphone.setExpectedSales(expectedSales);
                return smartphone;
            }).collect(Collectors.toList());

            smartphoneRepository.deleteAll();
            smartphoneRepository.insert(smartphones);

            smartphoneRepository.findAll();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getModelsByBrand(String brand) {
        return smartphoneRepository.findByBrand(brand).stream().map(BrandProjection::getBrand).collect(Collectors.toList());
    }
}
