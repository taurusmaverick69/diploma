package com.maverick.service;

import com.maverick.domain.data.Delivery;
import com.maverick.repository.DeliveryRepository;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    public List<Delivery> forecastOnNextSeason() {

        List<Delivery> deliveries = deliveryRepository.findAll();

        return smartphoneRepository.findByIsTrackedTrue().stream()

                .filter(smartphone -> deliveries.stream().anyMatch(delivery -> delivery.getSmartphone().equals(smartphone)))
                .map(smartphone -> {
                    Delivery delivery = new Delivery();
                    delivery.setSmartphone(smartphone);

                    Delivery lastDelivery = deliveryRepository.findFirstBySmartphoneOrderByDateDesc(smartphone);
                    Integer lastQuantity = lastDelivery.getQuantity();
                    LocalDate dateOfLastDelivery = lastDelivery.getDate();
                    delivery.setBought(lastQuantity);
//                    Integer sold = saleRepository.countBySmartphoneAndDateBetween(smartphone, dateOfLastDelivery, LocalDate.now());
//                    delivery.setSold(sold);
//                    int toBuy = sold - (lastQuantity - sold);

//                    delivery.setExpectedQuantity(toBuy);
                    delivery.setDate(dateOfLastDelivery);
                    return delivery;
                }).collect(Collectors.toList());
    }

    public void save(List<Delivery> deliveries) {
        deliveries.forEach(delivery -> delivery.setDate(LocalDate.now()));
        deliveryRepository.saveAll(deliveries);
    }

//    private Integer calculateOnNextSeason(Smartphone smartphone) {
//        Delivery lastDelivery = deliveryRepository.findFirstBySmartphoneOrderByDateDesc(smartphone);
//        Integer lastQuantity = lastDelivery.getExpectedQuantity();
//        LocalDate dateOfLastDelivery = lastDelivery.getDate();
//        Integer sold = saleRepository.countBySmartphoneAndDateBetween(smartphone, dateOfLastDelivery, LocalDate.now());
////        int toBuy = sold - (lastQuantity - sold) * raiting * season;
////        return toBuy;
//        return 0;
//    }
}
