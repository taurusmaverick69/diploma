package com.maverick.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Brand {

    @Id
    @GeneratedValue
    private Integer id;
    private String brand;
}
