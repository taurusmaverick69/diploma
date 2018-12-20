package com.maverick.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Data
@EqualsAndHashCode(of = "id")
@Document
public class SeasonCoefficient {

    @Id
    private ObjectId id;
    private String country;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double coefficient;
}
