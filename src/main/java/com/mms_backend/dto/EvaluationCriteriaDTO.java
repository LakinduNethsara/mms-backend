package com.mms_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EvaluationCriteriaDTO
{
    private int id;
    private String evaluationcriteria_id;

    private String course_id;

    private String type;

    private String assessment_type;

    private int no_of_conducted;

    private int no_of_taken;

    private int percentage;

    private String description;
}
