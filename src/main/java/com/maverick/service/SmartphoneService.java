package com.maverick.service;

import com.maverick.domain.Smartphone;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SmartphoneService {

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    public List<Smartphone> findAll() {
        return smartphoneRepository.findAll();
    }

    public static void main(String[] args) {

        UUID uuid = UUID.randomUUID();

        System.out.println(uuid);
    }

}
