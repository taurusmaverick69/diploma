package com.maverick.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Smartphone {

    @Id
    @GeneratedValue
    private Integer id;
    private String brand;
    private String model;
    private Integer releaseYear;
    private Double rating;
    private Integer quantity;

}
