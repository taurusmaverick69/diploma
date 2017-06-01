package com.maverick.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by sednor-7 on 01/06/17.
 */
@Entity
@Getter
@Setter
public class SeasonCoefficient {

    @Id
    @GeneratedValue
    private Integer id;
    private String country;
    @Enumerated(EnumType.STRING)
    private Season season;
    private Double coefficient;
}
