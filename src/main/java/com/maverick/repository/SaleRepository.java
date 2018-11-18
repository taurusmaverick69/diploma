package com.maverick.repository;

import com.maverick.domain.Sale;
import com.maverick.domain.Smartphone;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends MongoRepository<Sale, ObjectId> {

    List<Sale> findBySmartphone(Smartphone smartphone);

    Integer countBySmartphoneAndDateBetween(Smartphone smartphone, LocalDate startDate, LocalDate endDate);


}
