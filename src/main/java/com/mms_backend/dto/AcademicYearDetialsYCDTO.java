package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AcademicYearDetialsYCDTO {
    private int id;
    private String previous_academic_year;
    private String current_academic_year;
    private String current_semester;
}
