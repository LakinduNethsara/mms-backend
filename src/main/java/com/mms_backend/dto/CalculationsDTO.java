package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalculationsDTO
{
    private int id;
    private String student_id;
    private String course_id;
    private String mark;
    private String percentage;
    private String academic_year;
    private String evaluation_criteria_id;

}
