package com.mms_backend.dto.AR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarksApprovalLevelDTO {
    private int approval_id;
    private String course_id;
    private String academic_year;
    private String approval_level;
    private String department_id;
}
