package com.maverick.config;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component

public class DBObjectToYearMonthConverter implements Converter<Document, YearMonth> {
    @Override
    public YearMonth convert(Document source) {
        return YearMonth.of(
                (int) source.get("year"),
                (int) source.get("month")
        );
    }
}