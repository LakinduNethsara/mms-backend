package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentRegisteredCourses
{
    private int id;
    private String student_id;
    private String course_id;
    private String academic_year;
}
