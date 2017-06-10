package com.maverick.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Getter
@Setter
public class Sale {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Smartphone smartphone;
    private Date date;
    private Integer clientId;
}
