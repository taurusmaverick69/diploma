package com.maverick.service;

import com.maverick.domain.Smartphone;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;

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

}
