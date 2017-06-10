package com.maverick.service;

import com.maverick.domain.SeasonCoefficient;
import com.maverick.repository.SeasonCoefficientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeasonCoefficientService {

    @Autowired
    private SeasonCoefficientRepository seasonCoefficientRepository;

    public List<SeasonCoefficient> findAll() {
        return seasonCoefficientRepository.findAll();
    }

    public void save(SeasonCoefficient seasonCoefficient) {
        seasonCoefficientRepository.save(seasonCoefficient);
    }
}
