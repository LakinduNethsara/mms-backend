package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EvaluationCriteriaNameDTO {
    private int id;
    private String evaluationcriteria_id;
    private String assignment_name;
    private String course_id;
}
