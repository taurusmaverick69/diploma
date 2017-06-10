package com.maverick.service;

import com.maverick.repository.SeasonCoefficientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeasonCoefficientService {

    @Autowired
    private SeasonCoefficientRepository seasonCoefficientRepository;

}
