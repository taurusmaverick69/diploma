package com.maverick.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Smartphone {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Brand brand;
    private String model;
    private Double price;
    private Integer releaseYear;
    private Double rating;
    private Integer quantity;
    private Boolean isTracked;

}
