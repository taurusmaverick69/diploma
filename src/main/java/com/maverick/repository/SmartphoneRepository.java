package com.maverick.repository;

import com.maverick.domain.document.Smartphone;
import com.maverick.domain.projection.StatisticProjection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.SortedSet;

public interface SmartphoneRepository extends MongoRepository<Smartphone, ObjectId> {

    List<Smartphone> findByIsTrackedTrue();

    <T> SortedSet<T> findAllBy(Class<T> projection);

    StatisticProjection findByIdIs(ObjectId id);
}
