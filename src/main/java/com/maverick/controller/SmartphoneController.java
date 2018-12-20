package com.maverick.controller;

import com.maverick.domain.projection.BrandModelProjection;
import com.maverick.domain.projection.SalesProjection;
import com.maverick.service.SmartphoneService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.SortedSet;

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
    public String getBrandModels(Model model) {
        SortedSet<BrandModelProjection> brandModels = smartphoneService.getBrandModels();
        model.addAttribute("brandModels", brandModels);
        return "newsales";
    }


    @GetMapping(value = "/sales", params = "id")
    @ResponseBody
    public SalesProjection getSales(@RequestParam String id) {
        ObjectId objectId = new ObjectId(id);
        return smartphoneService.getSalesById(objectId);
    }


    @PostMapping("/reset")
    @ResponseBody
    public void resetData() throws IOException {
        smartphoneService.resetData();
    }
}
