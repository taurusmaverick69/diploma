package com.maverick.repository;

import com.maverick.domain.Smartphone;
import com.maverick.domain.projection.BrandModelProjection;
import com.maverick.domain.projection.SalesProjection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.SortedSet;

public interface SmartphoneRepository extends MongoRepository<Smartphone, ObjectId> {

    List<Smartphone> findByIsTrackedTrue();

    SortedSet<BrandModelProjection> findAllBy();

    SalesProjection findByIdIs(ObjectId id);
}
