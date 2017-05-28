package com.maverick.controller;

import com.maverick.domain.Smartphone;
import com.maverick.repository.SmartphoneRepository;
import com.maverick.service.SmartphoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SmartphoneController {

    @Autowired
    private SmartphoneService smartphoneService;

    @GetMapping
    public List<Smartphone> findAll() {
        return smartphoneService.findAll();
    }
}
