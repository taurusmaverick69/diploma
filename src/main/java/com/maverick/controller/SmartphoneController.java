package com.maverick.controller;

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
        model.addAttribute("smartphones", smartphoneService.findAll());
        return "main";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public void updateTracking(@PathVariable Integer id, @RequestParam Boolean isTracked) {
        smartphoneService.updateTracking(id, isTracked);
    }
}
