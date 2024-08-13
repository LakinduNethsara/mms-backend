package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GPADTO
{
    private int id;

    private String student_id;

    private String acadamic_year;

    private int level;

    private int semester;

    private String  sgpa;

    private String cgpa;
}
