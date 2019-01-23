package com.maverick.domain.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.StreamUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
