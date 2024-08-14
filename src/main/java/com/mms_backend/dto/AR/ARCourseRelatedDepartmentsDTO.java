package com.mms_backend.dto.AR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ARCourseRelatedDepartmentsDTO {
    private int id;
    private String course_id;
    private String department_id;
    private String credit;
}
