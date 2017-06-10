package com.maverick.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by sednor-7 on 01/06/17.
 */
@Entity
@Data
public class SeasonCoefficient {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Country country;
    private Date startDate;
    private Date endDate;
    private Double coefficient;
}
