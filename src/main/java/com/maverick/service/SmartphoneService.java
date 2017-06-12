package com.maverick.service;

import com.maverick.domain.Smartphone;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

    public Smartphone findById(Integer id) {
        return smartphoneRepository.findOne(id);
    }

    public void updateTracking(Integer id, Boolean isTracked) {
        Smartphone smartphone = smartphoneRepository.findOne(id);
        smartphone.setIsTracked(isTracked);
        smartphoneRepository.save(smartphone);
    }

    public List<Integer> findYears(Integer id) {
        return IntStream.rangeClosed(smartphoneRepository.findOne(id).getReleaseYear(), Year.now().getValue()).boxed().collect(toList());
    }

    public Boolean isTimeToDelivery() {
        List<Month> lastMonthsOfSeason = Arrays.asList(FEBRUARY, MAY, AUGUST, NOVEMBER);
        LocalDate currentLocalDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Month currentMonth = currentLocalDate.getMonth();
        int currentDayOfMonth = currentLocalDate.getDayOfMonth();
        return lastMonthsOfSeason.contains(currentMonth) && currentMonth.maxLength() == currentDayOfMonth;
    }
}
