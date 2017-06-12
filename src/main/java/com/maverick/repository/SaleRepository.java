package com.maverick.repository;

import com.maverick.domain.Sale;
import com.maverick.domain.Smartphone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findBySmartphone(Smartphone smartphone);

    Integer countBySmartphoneAndDateBetween(Smartphone smartphone,  Date startDate, Date endDate);


}
