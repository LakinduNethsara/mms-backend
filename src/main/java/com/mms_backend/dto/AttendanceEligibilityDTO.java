package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendanceEligibilityDTO
{
    private int id;

    private String student_id;

    private String course_id;

    private double percentage;

    private String eligibility;
}
