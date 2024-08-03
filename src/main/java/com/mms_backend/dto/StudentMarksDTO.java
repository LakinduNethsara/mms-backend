package com.mms_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentMarksDTO
{
    private int id;

    private String student_id;

    private String course_id;

    private String level;

    private String semester;

    private String total_ca_mark;

    private String ca_eligibility;

    private String total_final_mark;

    private String total_rounded_mark;

    private String grade;

    private String gpv;

}
