package com.mms_backend.dto.AR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDTO {
    private int id;
    private String course_id;
    private String course_name;
    private int hours;
    private String type;
    private String department_id;
    private int level;
    private int semester;
}
