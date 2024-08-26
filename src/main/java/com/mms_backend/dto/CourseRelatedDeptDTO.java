package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseRelatedDeptDTO {
    private int id;
    private String course_id;
    private String department_id;
    private double credit;
    private String gpa_ngpa;
}
