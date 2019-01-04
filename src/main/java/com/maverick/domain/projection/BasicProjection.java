package com.maverick.domain.projection;

import lombok.Value;
import org.bson.types.ObjectId;

import java.util.Comparator;

@Value
public class BasicProjection implements Comparable<BasicProjection> {

    private final Comparator<BasicProjection> COMPARATOR = Comparator
            .comparing(BasicProjection::getBrand)
            .thenComparing(BasicProjection::getModel);

    ObjectId id;
    String brand;
    String model;
    Double price;
    Integer releaseYear;
    Double rating;
    Integer quantity;
    Boolean isTracked;

    @Override
    public int compareTo(BasicProjection o) {
        return COMPARATOR.compare(this, o);
    }
}
