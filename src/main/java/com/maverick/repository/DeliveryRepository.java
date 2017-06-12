package com.maverick.repository;

import com.maverick.domain.Delivery;
import com.maverick.domain.Smartphone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    Delivery findFirstBySmartphoneOrderByDateDesc(Smartphone smartphone);

}
