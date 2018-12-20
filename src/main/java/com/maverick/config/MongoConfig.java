package com.maverick.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import io.github.cbartosiak.bson.codecs.jsr310.yearmonth.YearMonthAsDocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

@Configuration
@EnableMongoRepositories(basePackages = "com.maverick.repository")
@EntityScan(basePackages = "com.maverick.domain")
public class MongoConfig extends AbstractMongoConfiguration    {

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Bean
    public MongoClient mongoClient() {
//        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(CodecRegistries.fromCodecs(new YearMonthAsDocumentCodec()),
//                MongoClient.getDefaultCodecRegistry());
//        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
        return new MongoClient("localhost");
    }

    @Override
    public CustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new DBObjectToYearMonthConverter(),
                new YearMonthToDBObjectConverter()
        ));
    }

}
