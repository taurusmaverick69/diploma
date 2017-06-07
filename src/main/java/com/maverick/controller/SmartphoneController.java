package com.maverick.controller;

import com.maverick.domain.*;
import com.maverick.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Controller
public class SmartphoneController {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SeasonCoefficientRepository seasonCoefficientRepository;

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    @GetMapping("/main")
    public String main(Model model) {

        List<Integer> years =
                IntStream.rangeClosed(1996, Year.now().getValue()).boxed().collect(toList());
        model.addAttribute("smartphones", smartphoneRepository.findAll());
        //   model.addAttribute("seasons", Arrays.stream(Season.values()).map(Enum::toString).collect(toList()));
        List<Sale> bySmartphone = saleRepository.findBySmartphone(smartphoneRepository.findOne(1));
        model.addAttribute("numbers", bySmartphone.stream().map(Sale::getQuantity).collect(toList()));
        return "main";
    }

    @GetMapping("/statistics/{id}")
    public String showStatistics(@PathVariable Integer id, Model model) {
        // model.addAttribute("seasons", Arrays.stream(Season.values()).map(Enum::toString).collect(toList()));
        return "statistics";
    }

    @GetMapping("/testAll")
    public ResponseEntity test() {
        List<Brand> all = brandRepository.findAll();
        List<Country> all1 = countryRepository.findAll();
        List<Delivery> all2 = deliveryRepository.findAll();
        List<Sale> all3 = saleRepository.findAll();
        List<SeasonCoefficient> all4 = seasonCoefficientRepository.findAll();
        List<Smartphone> all5 = smartphoneRepository.findAll();
        return new ResponseEntity(HttpStatus.OK);
    }
}
