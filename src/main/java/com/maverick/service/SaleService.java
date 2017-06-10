package com.maverick.service;

import com.maverick.domain.Sale;
import com.maverick.repository.SaleRepository;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.stream.Stream;

import static java.time.ZoneId.systemDefault;
import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.*;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    public List<Long> findBySmartphoneId(Integer id) {

        List<Sale> sales = saleRepository.findBySmartphone(smartphoneRepository.findOne(id));
        Map<Month, Long> existedMonthNumberOfSales = sales
                .stream()
                .collect(groupingBy(sale -> Month.of(sale.getDate().toInstant().atZone(systemDefault()).toLocalDate().getMonthValue()), counting()));

        Set<Month> existedMonths = existedMonthNumberOfSales.keySet();
        Map<Month, Long> notExistedMonthNumberOfSales = Arrays.stream(Month.values()).filter(month -> !existedMonths.contains(month)).collect(toMap(o -> o, o -> 0L));

        return Stream.of(existedMonthNumberOfSales, notExistedMonthNumberOfSales)
                .flatMap(m -> m.entrySet().stream())
                .sorted(comparingByKey())
                .map(Map.Entry::getValue).collect(toList());
    }
}
