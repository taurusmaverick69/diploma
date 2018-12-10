package com.maverick.service;

import com.maverick.domain.Smartphone;
import com.maverick.repository.SmartphoneRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.extra.YearQuarter;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.IntStream;

import static java.time.Month.*;
import static java.util.stream.Collectors.toList;

@Service
public class SmartphoneService {

    @Autowired
    private SmartphoneRepository smartphoneRepository;

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
        return IntStream.rangeClosed(smartphoneRepository.findById(id).get().getReleaseYear(), Year.now().getValue()).boxed().collect(toList());
    }

    public Boolean isTimeToDelivery() {
        List<Month> lastMonthsOfSeason = Arrays.asList(FEBRUARY, MAY, AUGUST, NOVEMBER);
        LocalDate currentLocalDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Month currentMonth = currentLocalDate.getMonth();
        int currentDayOfMonth = currentLocalDate.getDayOfMonth();
        return lastMonthsOfSeason.contains(currentMonth) && currentMonth.maxLength() == currentDayOfMonth;
    }

    public static void main(String[] args) {

        Map<YearQuarter, Map<String, Integer>> map = Map.of(
                YearQuarter.of(2017, 1), Map.of(
                        "Samsung", 786714000,
                        "Apple", 519925000,
                        "Huawei", 341812000,
                        "Oppo", 309223000,
                        "Vivo" , 258422000
                ),
                YearQuarter.of(2017, 2), Map.of(
                        "Samsung", 825351000,
                        "Apple", 443148000,
                        "Huawei", 359643000,
                        "Oppo", 260925000,
                        "Vivo" , 243246000
                ),
                YearQuarter.of(2017, 3), Map.of(
                        "Samsung", 856053000,
                        "Apple", 454419000,
                        "Huawei", 365018000,
                        "Oppo", 294492000,
                        "Xiaomi" , 268532000
                ),
                YearQuarter.of(2017, 4), Map.of(
                        "Samsung", 740266000,
                        "Apple", 731752000,
                        "Huawei", 438870000,
                        "Xiaomi" , 281878000,
                        "Oppo", 256601000
                ));

    }
}
