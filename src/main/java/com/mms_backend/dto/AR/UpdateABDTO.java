package com.mms_backend.dto.AR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateABDTO {
    private String course_id;
    private String student_id;
    private String new_score;
    private String marks_table_exam_type;
    private String academic_year;
    private String exam_type;


}
