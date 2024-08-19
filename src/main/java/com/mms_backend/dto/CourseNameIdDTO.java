package com.mms_backend.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CourseNameIdDTO
{
    private String course_id;
    private String course_name;
    private String department_id;
    private String academic_year;
}
