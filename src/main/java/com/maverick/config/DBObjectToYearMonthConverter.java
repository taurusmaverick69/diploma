package com.maverick.config;

import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component

public class DBObjectToYearMonthConverter implements Converter<Object, YearMonth> {
    @Override
    public YearMonth convert(Object source) {
        return YearMonth.now();
//        return YearMonth.of(
//                (int) source.get("year"),
//                (int) source.get("month")
//        );
    }
}