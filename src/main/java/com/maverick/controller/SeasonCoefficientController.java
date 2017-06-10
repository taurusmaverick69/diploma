package com.maverick.controller;

import com.maverick.domain.SeasonCoefficient;
import com.maverick.service.CountryService;
import com.maverick.service.SeasonCoefficientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coefficient")
public class SeasonCoefficientController {

    @Autowired
    private SeasonCoefficientService seasonCoefficientService;

    @Autowired
    private CountryService countryService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("coefficients", seasonCoefficientService.findAll());
        model.addAttribute("countries", countryService.findAll());
        model.addAttribute("coefficient", new SeasonCoefficient());
        return "coefficients";
    }

    @PostMapping
    public String save(@ModelAttribute SeasonCoefficient seasonCoefficient) {
        seasonCoefficientService.save(seasonCoefficient);
        return "redirect:/coefficient";
    }

}
