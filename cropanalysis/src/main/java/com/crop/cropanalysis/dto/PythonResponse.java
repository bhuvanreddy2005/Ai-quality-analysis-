package com.crop.cropanalysis.dto;

import lombok.Data;

@Data
public class PythonResponse {

    private double health;
    private String grade;
    private String disease;
}
