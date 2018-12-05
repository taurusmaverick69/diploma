package com.maverick.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(of = "brand")
@Document
public class Smartphone {

    @Id
    private String brand;
    private String model;
    private Double price;
    private Integer releaseYear;
    private Double rating;
    private Integer quantity;
    private Boolean isTracked;

}
