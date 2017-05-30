package com.maverick.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class SmartphoneController {

    @GetMapping("/test")
    public String findAll() {
        throw new NullPointerException();
    }

    @GetMapping("/")
    public String findAll2(Model model) {
        model.addAttribute("serverTime", new Date());
        return "test";
    }

}
