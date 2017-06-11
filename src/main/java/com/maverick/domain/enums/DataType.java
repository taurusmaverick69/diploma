package com.maverick.domain.enums;

import lombok.Getter;

@Getter
public enum DataType {

    MONTHS("months"), SEASONS("seasons");

    private String type;

    DataType(String type) {
        this.type = type;
    }
}
