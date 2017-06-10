package com.maverick.controller;

import com.maverick.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Month;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping("/{id}")
    public String findBySmartphoneId(@PathVariable Integer id, Model model) {
        model.addAttribute("months", Arrays.stream(Month.values()).map(Enum::toString).collect(toList()));
        model.addAttribute("data", saleService.findBySmartphoneId(id));
        return "sales";
    }
}
