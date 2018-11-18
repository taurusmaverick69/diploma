package com.maverick.repository;

import com.maverick.domain.Delivery;
import com.maverick.domain.Smartphone;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeliveryRepository extends MongoRepository<Delivery, ObjectId> {

    Delivery findFirstBySmartphoneOrderByDateDesc(Smartphone smartphone);

}
