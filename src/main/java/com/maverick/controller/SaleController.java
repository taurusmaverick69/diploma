package com.maverick.controller;

import com.maverick.domain.Sale;
import com.maverick.repository.SaleRepository;
import com.maverick.repository.SmartphoneRepository;
import com.maverick.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.ZoneId.systemDefault;

@Controller
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SmartphoneRepository smartphoneRepository;


    @GetMapping
    @ResponseBody
    public void test() {


        List<Sale> sales = saleRepository.findBySmartphone(smartphoneRepository.findOne(1));
        Map<Month, List<Sale>> collect = sales.stream().collect(Collectors.groupingBy(sale -> {
            LocalDate localDate = sale.getDate().toInstant().atZone(systemDefault()).toLocalDate();
            return Month.of(localDate.getMonthValue());
        }));


    }
}
