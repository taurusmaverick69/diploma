package com.maverick.service;

import com.maverick.domain.enums.DataType;
import com.maverick.domain.enums.Season;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    public Map<String, Map<Integer, List<Long>>> findDataBySmartphoneId(Integer id) {

//        List<Sale> sales = saleRepository.findBySmartphone(smartphoneRepository.findOne(id));
//
//        Map<Integer, List<Long>> dataByMonthType = sales.stream()
//                .collect(Collectors.Collectors.groupingBy(sale -> Year.of(sale.getDate().toInstant().atZone(systemDefault()).toLocalDate().getYear()).getValue()))
//                .entrySet()
//                .stream()
//                .collect((Collectors.Collectors.toMap(Map.Entry::getKey, pair -> {
//                    Map<Month, Long> existedMonthNumberOfSales =
//                            pair.getValue()
//                                    .stream()
//                                    .collect(Collectors.groupingBy(sale -> Month.of(sale.getDate().toInstant().atZone(systemDefault()).toLocalDate().getMonthValue()), counting()));
//                    Set<Month> existedMonths = existedMonthNumberOfSales.keySet();
//                    Map<Month, Long> notExistedMonthNumberOfSales = Arrays.stream(Month.values()).filter(month -> !existedMonths.contains(month)).collect(Collectors.toMap(o -> o, o -> 0L));
//                    return Stream.of(existedMonthNumberOfSales, notExistedMonthNumberOfSales)
//                            .flatMap(m -> m.entrySet().stream())
//                            .sorted(comparingByKey())
//                            .map(Map.Entry::getValue).collect(Collectors.toList());
//                })));
//
//        Map<Integer, List<Long>> dataBySeasonType = sales.stream()
//                .collect(Collectors.groupingBy(sale -> Year.of(sale.getDate().toInstant().atZone(systemDefault()).toLocalDate().getYear()).getValue()))
//                .entrySet()
//                .stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, pair -> {
//                    Map<Season, Long> existedSeasonNumberOfSales = pair.getValue()
//                            .stream()
//                            .collect(Collectors.groupingBy(sale -> Season.of(sale.getDate().toInstant().atZone(systemDefault()).toLocalDate().getMonth()), Collectors.counting()));
//                    Set<Season> existedSeasons = existedSeasonNumberOfSales.keySet();
//                    Map<Season, Long> notExistedMonthNumberOfSales = Arrays.stream(Season.values()).filter(season -> !existedSeasons.contains(season)).collect(Collectors.Collectors.toMap(o -> o, o -> 0L));
//                    return Stream.of(existedSeasonNumberOfSales, notExistedMonthNumberOfSales)
//                            .flatMap(m -> m.entrySet().stream())
//                            .sorted(Collectors.comparingByKey())
//                            .map(Map.Entry::getValue).collect(Collectors.toList());
//                }));
//
//        Map<String, Map<Integer, List<Long>>> completeData = new HashMap<>();
//        completeData.put(DataType.MONTHS.getType(), dataByMonthType);
//        completeData.put(DataType.SEASONS.getType(), dataBySeasonType);
//        return completeData;
        return null;
    }

    public Map<String, List<String>> findRanges() {
        List<String> months = Arrays.stream(Month.values()).map(Enum::toString).collect(Collectors.toList());
        List<String> seasons = Arrays.stream(Season.values()).map(Enum::toString).collect(Collectors.toList());
        Map<String, List<String>> ranges = new HashMap<>();
        ranges.put(DataType.MONTHS.getType(), months);
        ranges.put(DataType.SEASONS.getType(), seasons);
        return ranges;
    }
}
