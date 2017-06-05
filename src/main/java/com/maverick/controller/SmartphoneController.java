package com.maverick.controller;

import com.maverick.domain.Sale;
import com.maverick.domain.Season;
import com.maverick.repository.SaleRepository;
import com.maverick.repository.SeasonCoefficientRepository;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Controller
public class SmartphoneController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    @Autowired
    private SeasonCoefficientRepository seasonCoefficientRepository;

    @GetMapping("/main")
    public String main(Model model) {


        List<Integer> years =
                IntStream.rangeClosed(1996, Year.now().getValue()).boxed().collect(toList());


        model.addAttribute("smartphones", smartphoneRepository.findAll());
        List<Sale> bySmartphone = saleRepository.findBySmartphone(smartphoneRepository.findOne(1));
        model.addAttribute("numbers", bySmartphone.stream().map(Sale::getQuantity).collect(toList()));
        model.addAttribute("seasons", Arrays.stream(Season.values()).map(Enum::toString).collect(toList()));

        return "main";
    }


//    @PostMapping("/save")
//    public String save(@ModelAttribute Smartphone smartphone) {
//        System.out.println(smartphone);
//        return "new";
//    }
}
