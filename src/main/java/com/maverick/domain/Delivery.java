package com.maverick.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Delivery {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    private Smartphone smartphone;
    @Transient
    private Integer bought;
    @Transient
    private Integer sold;
    private Date date;
    private Integer quantity;
}
