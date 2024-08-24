package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarksRangeOfClassDTO
{
    private int id;

    private String course_id;

    private String grade;

    private double margin_of_grade;

    private String academic_year;

    private String user_id;

    private Date datetime;
}
