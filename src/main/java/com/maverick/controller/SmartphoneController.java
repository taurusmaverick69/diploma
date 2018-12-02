package com.maverick.controller;

import com.maverick.domain.Smartphone;
import com.maverick.repository.SmartphoneRepository;
import com.maverick.service.SmartphoneService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/smartphone")
public class SmartphoneController {

    @Autowired
    private SmartphoneService smartphoneService;

    @Autowired
    private SmartphoneRepository smartphoneRepository;



    @GetMapping
    public String findAll(Model model) {
        Smartphone entity = new Smartphone();
        entity.setBrand("TESTB");
        smartphoneRepository.insert(entity);
//        model.addAttribute("smartphones", smartphoneService.findAll());
//        model.addAttribute("isTimeToDelivery", smartphoneService.isTimeToDelivery());
        return "main";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public void updateTracking(@PathVariable ObjectId id, @RequestParam Boolean isTracked) {
        smartphoneService.updateTracking(id, isTracked);
    }

    @GetMapping("/test")
    public String test(Model model) {
        List<Smartphone> all = smartphoneService.findAll();
        System.out.println(all);
        model.addAttribute("smartphones", all);
        return "test";
    }
}
