package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDetailsDTO {
    private int id;
    private String student_id;
    private String full_name;
    private String name_with_initials;
    private String user_name;
    private String email;
    private String password;
    private Year registered_year;
    private String department_id;

}
