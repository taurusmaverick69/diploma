package com.maverick.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TestDto {

    private List<List<String>> data;
    private String val;
}
