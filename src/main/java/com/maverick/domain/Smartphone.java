package com.maverick.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.SortedSet;

@Data
@EqualsAndHashCode(of = {"brand", "model"})
@Document
public class Smartphone {

    @Id
    private ObjectId id;
    private String brand;
    private String model;
    private Double price;
    private Integer releaseYear;
    private Double rating;
    private Integer quantity;
    private Boolean isTracked;

    private SortedSet<Sale> expectedSales;
    private SortedSet<Sale> actualSales;
}


