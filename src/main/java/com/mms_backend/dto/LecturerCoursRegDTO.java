package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LecturerCoursRegDTO
{
    private int id;

    private String user_id;

    private String course_id;
}
