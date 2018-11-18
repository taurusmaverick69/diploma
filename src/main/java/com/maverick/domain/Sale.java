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
public class Sale {

    @Id
    private ObjectId id;
    private Smartphone smartphone;
    private LocalDate date;
    private Integer clientId;
}
