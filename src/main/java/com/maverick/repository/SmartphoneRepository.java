package com.maverick.repository;

import com.maverick.domain.Smartphone;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SmartphoneRepository extends MongoRepository<Smartphone, ObjectId> {

    List<Smartphone> findByIsTrackedTrue();
}
