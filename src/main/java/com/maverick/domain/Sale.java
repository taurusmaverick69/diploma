package com.maverick.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
public class Sale {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Smartphone smartphone;
    private Date date;
    private Integer clientId;
}
