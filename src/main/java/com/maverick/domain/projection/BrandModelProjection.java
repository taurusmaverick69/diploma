package com.maverick.domain.projection;

import lombok.Value;
import org.bson.types.ObjectId;

import java.util.Comparator;

@Value
public class BrandModelProjection implements Comparable<BrandModelProjection> {

    private final Comparator<BrandModelProjection> COMPARATOR = Comparator
            .comparing(BrandModelProjection::getBrand)
            .thenComparing(BrandModelProjection::getModel);

    ObjectId id;
    String brand;
    String model;

    @Override
    public int compareTo(BrandModelProjection o) {
        return COMPARATOR.compare(this, o);
    }
}

