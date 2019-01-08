package com.maverick.controller;

import com.maverick.domain.document.Smartphone;
import com.maverick.domain.projection.StatisticProjection;
import com.maverick.service.SmartphoneService;
import org.bson.types.ObjectId;
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
        model.addAttribute("smartphones", smartphoneService.findAll());
        model.addAttribute("isTimeToDelivery", smartphoneService.isTimeToDelivery());
        return "main";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public void updateTracking(@PathVariable ObjectId id, @RequestParam Boolean isTracked) {
        smartphoneService.updateTracking(id, isTracked);
    }

    @GetMapping(value = "/sales", params = "id")
    public String getStatisticById(Model model, @RequestParam String id) {
        ObjectId objectId = new ObjectId(id);
        StatisticProjection statistic = smartphoneService.getStatisticById(objectId);
        model.addAttribute("statistic", statistic);
        return "statistic";
    }

    @PutMapping
    public String update(@ModelAttribute Smartphone smartphone) {
        smartphoneService.update(smartphone);
        return "redirect:/smartphone/sales?id=" + smartphone.getId();
    }

    @GetMapping("/reset")
    public String resetData() {
        smartphoneService.resetData();
        return "redirect:/smartphone";
    }
}
