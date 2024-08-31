package com.mms_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentRegCoursesDTO
{

    private String student_id;

    private String course_id;

    private String academic_year;

    private int is_repeat;
}
