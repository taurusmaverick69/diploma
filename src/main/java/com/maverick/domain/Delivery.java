package com.maverick.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.client.AsyncRestTemplate;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = "id")
@Document
public class Delivery {

    @Id
    private ObjectId id;
    private Smartphone smartphone;
    @Transient
    private Integer bought;
    @Transient
    private Integer sold;
    private LocalDate date;
    private Integer quantity;
}
