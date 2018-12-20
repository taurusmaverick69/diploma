package com.maverick.config;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Map;

@Component
@WritingConverter
public class YearMonthToDBObjectConverter implements Converter<YearMonth, Document> {

    @Override
    public Document convert(YearMonth source) {
        return new Document(Map.of("year", source.getYear(), "month", source.getMonth()));
    }
}