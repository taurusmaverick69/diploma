package com.maverick.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maverick.domain.Sale;
import com.maverick.domain.Smartphone;
import com.maverick.domain.SmartphoneDto;
import com.maverick.repository.SmartphoneRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.threeten.extra.YearQuarter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.Month.*;

@Service
public class SmartphoneService {

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
        int uaPercentage = 100/uaPercent;

        Map<YearQuarter, Map<String, Integer>> smartphoneMap = Map.of(
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


        Map<YearQuarter, Integer> yearQuarterSumQuantityMap = smartphoneMap.entrySet().stream().collect(Collectors.toMap(
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

            List<Sale> expectedSales = yearQuarterSumQuantityMap.entrySet().stream().map(yearQuarterSumQuantity -> {
                Sale sale = new Sale();
                sale.setYearQuarter(yearQuarterSumQuantity.getKey());
                int regionQuantity = Math.round(yearQuarterSumQuantity.getValue() * popularity);
                sale.setQuantity(regionQuantity);
                return sale;
            }).collect(Collectors.toList());

            smartphone.setExpectedSales(expectedSales);
            return smartphone;
        }).collect(Collectors.toList());

        smartphoneRepository.deleteAll();
        smartphoneRepository.insert(smartphones);
    }
}
