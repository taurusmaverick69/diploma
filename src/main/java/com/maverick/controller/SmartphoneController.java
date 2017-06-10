package com.maverick.controller;

import com.maverick.domain.SeasonCoefficient;
import com.maverick.service.SmartphoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/smartphone")
public class SmartphoneController {

    @Autowired
    private SmartphoneService smartphoneService;

    @GetMapping
    public String findAll(Model model) {

//        List<Integer> years =
//                IntStream.rangeClosed(1996, Year.now().getValue()).boxed().collect(toList());

//        //   model.addAttribute("seasons", Arrays.stream(Season.values()).map(Enum::toString).collect(toList()));
//        List<Sale> bySmartphone = saleRepository.findBySmartphone(smartphoneRepository.findOne(1));
////        model.addAttribute("numbers", bySmartphone.stream().map(Sale::getQuantity).collect(toList()));
//        model.addAttribute("coefficient", new SeasonCoefficient());
//        List<Country> all = countryRepository.findAll();
//        model.addAttribute("countries", all);
        model.addAttribute("smartphones", smartphoneService.findAll());
        return "main";
    }

    @GetMapping("/statistics/{id}")
    public String showStatistics(@PathVariable Integer id, Model model) {
        // model.addAttribute("seasons", Arrays.stream(Season.values()).map(Enum::toString).collect(toList()));
        return "statistics";
    }

    @PostMapping("/coef")
    public String save(@ModelAttribute SeasonCoefficient seasonCoefficient) {
        System.out.println(seasonCoefficient);
        return "main";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public void updateTracking(@PathVariable Integer id, @RequestParam Boolean isTracked) {
        smartphoneService.updateTracking(id, isTracked);
    }
}
