package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssessmentTypeListDTO {
    private int id;
    private String assessment_type_name;
    private String ca_mid_end;
}
