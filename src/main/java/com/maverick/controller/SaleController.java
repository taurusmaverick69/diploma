package com.maverick.controller;

import com.maverick.domain.Smartphone;
import com.maverick.service.SaleService;
import com.maverick.service.SmartphoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SmartphoneService smartphoneService;

    @GetMapping("/{id}")
    public String findBySmartphoneId(@PathVariable Integer id, Model model) {

        Smartphone smartphone = smartphoneService.findById(id);
        model.addAttribute("smartphone", smartphone.getBrand().getBrand() + " " + smartphone.getModel());
        model.addAttribute("years", smartphoneService.findYears(id));
        model.addAttribute("ranges", saleService.findRanges());
        model.addAttribute("data", saleService.findDataBySmartphoneId(id));
        return "sales";
    }
}
