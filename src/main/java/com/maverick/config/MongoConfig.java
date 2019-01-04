package com.maverick.config;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.YearMonth;
import java.util.Arrays;

@Configuration
@EnableMongoRepositories(basePackages = "com.maverick.repository")
@EntityScan(basePackages = "com.maverick.domain")
public class MongoConfig extends AbstractMongoConfiguration {

    @Override
    public MongoClient mongoClient() {
        return new MongoClient("localhost");
    }

    @Override
    protected String getDatabaseName() {
        return "test";
    }

//    @Bean
//    public MongoClient mongoClient() {
//        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(new YearMonthAsStringCodec()),
//                MongoClient.getDefaultCodecRegistry());
//        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
//        return new MongoClient("localhost", options);
//    }

    @Override
    public CustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new StringYearMonthConverter(), new YearMonthStringConverter()
        ));
    }

    @WritingConverter
    public class YearMonthStringConverter implements Converter<YearMonth, String> {
        @Override
        public String convert(YearMonth source) {
            return source.toString();
        }
    }

    @ReadingConverter
    public class StringYearMonthConverter implements Converter<String, YearMonth> {
        @Override
        public YearMonth convert(String source) {
            return YearMonth.parse(source);
        }
    }
}
