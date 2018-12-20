package com.maverick.controller;

import com.maverick.domain.Smartphone;
import com.maverick.domain.TestDto;
import com.maverick.service.SmartphoneService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/smartphone")
public class SmartphoneController {

    @Autowired
    private SmartphoneService smartphoneService;

    @GetMapping
    public String findAll(Model model) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("traffic", "no-tablet");
        params.add("period", "");
        params.add("country", "by");
        params.add("val", "Apple+iPhone+7+Plus");
        params.add("type", "device_marketing");

        String uri = UriComponentsBuilder
                .fromPath("/device-data/explorer/ajax/map-data-public")
                .queryParams(params).build().toUriString();





        model.addAttribute("smartphones", smartphoneService.findAll());
        model.addAttribute("isTimeToDelivery", smartphoneService.isTimeToDelivery());
        return "main";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public void updateTracking(@PathVariable ObjectId id, @RequestParam Boolean isTracked) {
        smartphoneService.updateTracking(id, isTracked);
    }

    @GetMapping("/sales")
    public String getSales(Model model) {
        List<Smartphone> all = smartphoneService.findAll();
        System.out.println(all);
        model.addAttribute("smartphones", all);
        return "newsales";
    }

    @PostMapping("/reset")
    @ResponseBody
    public void resetData() throws IOException {
        smartphoneService.resetData();
    }

    @GetMapping("models")
    @ResponseBody
    public List<String> getModelsByBrand(@RequestParam String brand) {
        return smartphoneService.getModelsByBrand(brand);
    }
}
