package com.mms_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssigncertifylecturerDTO
{
    private int id;

    private String lecturer_id;

    private String course_id;
    private String department_id;
}