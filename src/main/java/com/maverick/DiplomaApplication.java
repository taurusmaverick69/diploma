package com.maverick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class DiplomaApplication {


    @Bean
    public RestTemplate deviceAtlasRestTemplate(RestTemplateBuilder builder) {
//        https://deviceatlas.com/device-data/explorer/ajax/map-data-public?traffic=no-tablet&period=&country=by&val=Apple+iPhone+7+Plus&type=device_marketing&_=1543319407169

        return builder.rootUri("https://deviceatlas.com/").build();
    }

    public static void main(String[] args) {
        SpringApplication.run(DiplomaApplication.class, args);
    }
}
