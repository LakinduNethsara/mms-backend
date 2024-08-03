package com.mms_backend.dto.AR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicalDTO {
    private int id;
    private String student_id;
    private String course_id;
    private String academic_year;
    private String exam_type;
    private String medical_state;


}
