package com.maverick.domain.document;

import com.maverick.domain.data.Sale;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.threeten.extra.YearQuarter;

import java.time.YearMonth;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

@Data
@EqualsAndHashCode(of = {"brand", "model"})
@Document
public class Smartphone {

    @Id
    private ObjectId id;
    private String brand;
    private String model;
    private Double price;
    private Integer releaseYear;
    private Double rating;
    private Integer quantity;
    private Boolean isTracked;

    private SortedSet<Sale> expectedSales;
    private SortedSet<Sale> calculatedSales;

    private SortedMap<YearMonth, Float> coefficients = new TreeMap<>();

    public static final Map<YearQuarter, Map<String, Integer>> YEAR_QUARTER_SMARTPHONE_QUANTITY_MAP = Map.of(
            YearQuarter.of(2017, 1),
            Map.of(
                    "Samsung", 786714000,
                    "Apple", 519925000,
                    "Xiaomi", 127073000
            ),
            YearQuarter.of(2017, 2),
            Map.of(
                    "Samsung", 825351000,
                    "Apple", 443148000,
                    "Xiaomi", 241785000
            ),
            YearQuarter.of(2017, 3),
            Map.of(
                    "Samsung", 856053000,
                    "Apple", 454419000,
                    "Xiaomi", 268532000
            ),
            YearQuarter.of(2017, 4),
            Map.of(
                    "Samsung", 740266000,
                    "Apple", 731752000,
                    "Xiaomi", 281878000
            ),
            YearQuarter.of(2018, 1),
            Map.of(
                    "Samsung", 785648000,
                    "Apple", 540589000,
                    "Xiaomi", 284982000
            ),
            YearQuarter.of(2018, 2),
            Map.of(
                    "Samsung", 723364000,
                    "Apple", 447151000,
                    "Xiaomi", 328255000
            ),
            YearQuarter.of(2018, 3),
            Map.of(
                    "Samsung", 733601000,
                    "Apple", 457466000,
                    "Xiaomi", 332197000
            )
    );

    public static final Map<YearMonth, Map<String, Float>> COUNTER_POINT_MAP =
            Map.of(
                    YearMonth.of(2017, 8), Map.of(

                            "Apple iPhone 7", 4.0f,
                            "Apple iPhone 7 Plus", 2.9f,
                            "Samsung Galaxy S8", 1.8f,
                            "Xiaomi Redmi Note 4X", 1.8f,
                            "Samsung Galaxy S8 Plus", 1.7f,
                            "Apple iPhone 6", 1.6f,
                            "Samsung Galaxy J7 Prime", 1.4f,
                            "Samsung Galaxy A5 2017", 1.2f
                    ),
                    YearMonth.of(2017, 9), Map.of(

                            "Apple iPhone 8", 4.6f,
                            "Apple iPhone 8 Plus", 4.0f,
                            "Samsung Galaxy Note 8", 2.4f,
                            "Samsung Galaxy S8 Plus", 1.5f,
                            "Apple iPhone 6", 1.2f,
                            "Apple iPhone 7", 1.2f,
                            "Samsung Galaxy S8", 1.2f,
                            "Xiaomi Redmi Note 4", 1.1f
                    ),
                    YearMonth.of(2017, 10), Map.of(

                            "Apple iPhone 8", 4.6f,
                            "Apple iPhone 8 Plus", 4.0f,
                            "Samsung Galaxy Note 8", 2.4f,
                            "Samsung Galaxy S8 Plus", 1.5f,
                            "Apple iPhone 6", 1.2f,
                            "Apple iPhone 7", 1.2f,
                            "Samsung Galaxy S8", 1.2f,
                            "Xiaomi Redmi Note 4", 1.1f
                    ),
                    YearMonth.of(2017, 11), Map.of(

                            "Apple iPhone X", 6.6f,
                            "Apple iPhone 8", 4.8f,
                            "Apple iPhone 8 Plus", 2.8f,
                            "Samsung Galaxy Note 8", 1.7f,
                            "Apple iPhone 7", 1.4f,
                            "Samsung Galaxy J7 Prime", 1.3f,
                            "Apple iPhone 6", 1.2f,
                            "Samsung Galaxy S8 Plus", 1.1f
                    ),
                    YearMonth.of(2017, 12), Map.of(

                            "Apple iPhone X", 6.6f,
                            "Apple iPhone 8", 4.8f,
                            "Apple iPhone 8 Plus", 2.8f,
                            "Samsung Galaxy Note 8", 1.7f,
                            "Apple iPhone 7", 1.4f,
                            "Samsung Galaxy J7 Prime", 1.3f,
                            "Apple iPhone 6", 1.2f,
                            "Samsung Galaxy S8 Plus", 1.1f
                    ),
                    YearMonth.of(2018, 1), Map.of(

                            "Apple iPhone X", 5.1f,
                            "Apple iPhone 8", 3.1f,
                            "Apple iPhone 7", 2.2f,
                            "Apple iPhone 8 Plus", 1.9f,
                            "Xiaomi Redmi 5A", 1.9f,
                            "Samsung Galaxy S8", 1.4f,
                            "Samsung Galaxy J7 Pro", 1.1f,
                            "Xiaomi Mi A1", 1.1f
                    ),
                    YearMonth.of(2018, 2), Map.of(

                            "Apple iPhone X", 5.1f,
                            "Apple iPhone 8", 3.1f,
                            "Apple iPhone 7", 2.2f,
                            "Apple iPhone 8 Plus", 1.9f,
                            "Xiaomi Redmi 5A", 1.9f,
                            "Samsung Galaxy S8", 1.4f,
                            "Samsung Galaxy J7 Pro", 1.1f,
                            "Xiaomi Mi A1", 1.1f
                    ),
                    YearMonth.of(2018, 3), Map.of(

                            "Apple iPhone X", 3.5f,
                            "Apple iPhone 8 Plus", 2.3f,
                            "Xiaomi Redmi 5A", 1.8f,
                            "Samsung Galaxy S9", 1.6f,
                            "Samsung Galaxy S9 Plus", 1.6f,
                            "Apple iPhone 7", 2.6f,
                            "Apple iPhone 8", 3.4f,
                            "Samsung Galaxy J7 Pro", 1.4f,
                            "Apple iPhone 6", 1.2f
                    ),
                    YearMonth.of(2018, 4), Map.of(

                            "Samsung Galaxy S9", 2.6f,
                            "Samsung Galaxy S9 Plus", 2.6f,
                            "Apple iPhone X", 2.3f,
                            "Apple iPhone 8 Plus", 2.3f,
                            "Apple iPhone 8", 2.2f,
                            "Xiaomi Redmi 5A", 1.5f,
                            "Apple iPhone 6", 1.4f,
                            "Apple iPhone 7", 1.4f,
                            "Samsung Galaxy S8", 1.3f
                    ),
                    YearMonth.of(2018, 5), Map.of(

                            "Apple iPhone 8", 2.2f,
                            "Samsung Galaxy S9 Plus", 2.6f,
                            "Apple iPhone X", 2.3f,
                            "Xiaomi Redmi 5A", 1.5f,
                            "Apple iPhone 8 Plus", 2.3f,
                            "Samsung Galaxy S9", 2.6f
                    )
            );
}