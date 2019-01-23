package com.maverick.domain.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Sale {

    private int expectedQuantity;
    private int customCoefficientDisabledQuantity;
    private int customCoefficientRatingEnabledQuantity;
    private int customCoefficientSeasonEnabledOnlyQuantity;
    private int customCoefficientEnabledQuantity;
    private int movingAverageQuantity;
}
