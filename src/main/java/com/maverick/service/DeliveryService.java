package com.maverick.service;

import com.maverick.domain.Delivery;
import com.maverick.domain.Smartphone;
import com.maverick.repository.DeliveryRepository;
import com.maverick.repository.SaleRepository;
import com.maverick.repository.SmartphoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    @Autowired
    private SaleRepository saleRepository;

    public List<Delivery> forecastOnNextSeason() {

        List<Delivery> deliveries = deliveryRepository.findAll();

        return smartphoneRepository.findByIsTrackedTrue().stream()

                .filter(smartphone -> deliveries.stream().anyMatch(delivery -> delivery.getSmartphone().equals(smartphone)))
                .map(smartphone -> {
                    Delivery delivery = new Delivery();
                    delivery.setSmartphone(smartphone);

                    Delivery lastDelivery = deliveryRepository.findFirstBySmartphoneOrderByDateDesc(smartphone);
                    Integer lastQuantity = lastDelivery.getQuantity();
                    Date dateOfLastDelivery = lastDelivery.getDate();
                    delivery.setBought(lastQuantity);
                    Integer sold = saleRepository.countBySmartphoneAndDateBetween(smartphone, dateOfLastDelivery, new Date());
                    delivery.setSold(sold);
                    int toBuy = sold - (lastQuantity - sold);

                    delivery.setQuantity(toBuy);
                    delivery.setDate(dateOfLastDelivery);
                    return delivery;
                }).collect(toList());
    }

    public void save(List<Delivery> deliveries) {
        deliveries.forEach(delivery -> delivery.setDate(new Date()));
        deliveryRepository.save(deliveries);
    }

    private Integer calculateOnNextSeason(Smartphone smartphone) {
        Delivery lastDelivery = deliveryRepository.findFirstBySmartphoneOrderByDateDesc(smartphone);
        Integer lastQuantity = lastDelivery.getQuantity();
        Date dateOfLastDelivery = lastDelivery.getDate();
        Integer sold = saleRepository.countBySmartphoneAndDateBetween(smartphone, dateOfLastDelivery, new Date());
        int toBuy = sold - (lastQuantity - sold) * raiting * season;
        return toBuy;
    }
}
