package com.maverick.repository;

import com.maverick.domain.data.SeasonCoefficient;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SeasonCoefficientRepository extends MongoRepository<SeasonCoefficient, ObjectId> {
}
