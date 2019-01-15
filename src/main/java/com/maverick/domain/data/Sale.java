package com.maverick.domain.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.util.Pair;

import java.time.YearMonth;
import java.util.Map;

@Getter
@Setter
@ToString
public class Sale {

    private int expectedQuantity;
    private int customCoefficientQuantity;
    private int movingAverageQuantity;

    public static void main(String[] args) {


        int couldSold = 0;
        int redundantSold = 0;


        Pair<Integer, Integer> evaluation = Pair.of(0, 0);
        Map<YearMonth, Integer> map = Map.of(YearMonth.of(2017, 1), 1000, YearMonth.of(2017, 2), 1000, YearMonth.of(2017, 3), -2000);

        for (Integer count : map.values()) {

            if (count < 0) {
                int compensated = count + redundantSold;
                if (compensated > 0) {
                    redundantSold += count;
                } else {
                    redundantSold = 0;
                    couldSold += compensated;
                }
            } else {
                redundantSold += count;
            }
        }
        System.out.println("couldSold = " + couldSold);
        System.out.println("redundantSold = " + redundantSold);
    }

}
