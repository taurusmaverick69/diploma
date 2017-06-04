package com.maverick.controller;

import com.maverick.domain.Smartphone;
import com.maverick.repository.SaleRepository;
import com.maverick.repository.SeasonCoefficientRepository;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class SmartphoneController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    @Autowired
    private SeasonCoefficientRepository seasonCoefficientRepository;

//    @GetMapping("/")
//    public String findAll(Model model, HttpSession session) {
//
//        Smartphone smartphone = new Smartphone();
//        smartphone.setReleaseYear(2016);
//        model.addAttribute(smartphone);
//
//        session.setAttribute("test", "111");
//
////        model.addAttribute("smartphones", smartphoneRepository.findAll());
////        System.out.println(model);
//        return "main";
//    }
//
//    @PostMapping("/save")
//    public String save(@ModelAttribute Smartphone smartphone) {
//        System.out.println(smartphone);
//        return "new";
//    }
}
