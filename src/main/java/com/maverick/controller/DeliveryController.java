package com.maverick.controller;

import com.maverick.domain.Delivery;
import com.maverick.domain.enums.Season;
import com.maverick.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static java.time.ZoneId.systemDefault;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("deliveries", deliveryService.forecastOnNextSeason());
        Season nextSeason = Season.of(new Date().toInstant().atZone(systemDefault()).toLocalDate().plusMonths(1).getMonth());
        model.addAttribute("nextSeason", nextSeason);
        return "deliveries";
    }

    @PostMapping
    @ResponseBody
    public void save(@RequestBody List<Delivery> deliveries) {
        deliveryService.save(deliveries);
    }
}
