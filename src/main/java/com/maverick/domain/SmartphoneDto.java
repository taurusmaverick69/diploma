package com.maverick.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmartphoneDto {

    @JsonProperty("device_model")
    private String deviceModel;
    private float popularity;
}
