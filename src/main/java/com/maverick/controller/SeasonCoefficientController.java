package com.maverick.controller;

import com.maverick.domain.SeasonCoefficient;
import com.maverick.service.SeasonCoefficientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coefficient")
public class SeasonCoefficientController {

    @Autowired
    private SeasonCoefficientService seasonCoefficientService;

}
