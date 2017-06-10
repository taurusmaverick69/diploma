package com.maverick.service;

import com.maverick.domain.Smartphone;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmartphoneService {

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    public List<Smartphone> findAll() {
        return smartphoneRepository.findAll();
    }

    public void updateTracking(Integer id, Boolean isTracked) {
        Smartphone smartphone = smartphoneRepository.findOne(id);
        smartphone.setIsTracked(isTracked);
        smartphoneRepository.save(smartphone);
    }
}
