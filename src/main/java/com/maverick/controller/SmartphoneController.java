package com.maverick.controller;

import com.maverick.domain.Sale;
import com.maverick.domain.Season;
import com.maverick.domain.SeasonCoefficient;
import com.maverick.domain.Smartphone;
import com.maverick.repository.SaleRepository;
import com.maverick.repository.SeasonCoefficientRepository;
import com.maverick.repository.SmartphoneRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

@RestController
public class SmartphoneController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    @Autowired
    private SeasonCoefficientRepository seasonCoefficientRepository;

    @GetMapping("/")
    public List<Sale> findAll() {

        List<String> countries = Arrays.stream(Locale.getISOCountries())
                .map(s -> new Locale("", s).getDisplayCountry())
                .collect(toList());

        List<SeasonCoefficient> collect = countries.stream().map(country -> {

            SeasonCoefficient winterCoefficient = new SeasonCoefficient();
            winterCoefficient.setCountry(country);
            winterCoefficient.setSeason(Season.WINTER);
            winterCoefficient.setCoefficient(1.3);

            SeasonCoefficient springCoefficient = new SeasonCoefficient();
            springCoefficient.setCountry(country);
            springCoefficient.setSeason(Season.SPRING);
            springCoefficient.setCoefficient(1.1);

            SeasonCoefficient summerCoefficient = new SeasonCoefficient();
            summerCoefficient.setCountry(country);
            summerCoefficient.setSeason(Season.SUMMER);
            summerCoefficient.setCoefficient(1.0);

            SeasonCoefficient autumnCoefficient = new SeasonCoefficient();
            autumnCoefficient.setCountry(country);
            autumnCoefficient.setSeason(Season.AUTUMN);
            autumnCoefficient.setCoefficient(1.0);

            return Arrays.asList(winterCoefficient, springCoefficient, summerCoefficient, autumnCoefficient);

        }).flatMap(List::stream).collect(toList());

        seasonCoefficientRepository.save(collect);

        return saleRepository.findAll();
    }

}
