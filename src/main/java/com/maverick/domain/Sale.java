package com.maverick.domain;

import lombok.*;

import javax.persistence.*;

/**
 * Created by sednor-7 on 01/06/17.
 */
@Entity
@Data
public class Sale {

    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "smartphone_id")
    private Smartphone smartphone;
    private Integer year;
    @Enumerated(EnumType.STRING)
    private Season season;
    private Integer quantity;
}
