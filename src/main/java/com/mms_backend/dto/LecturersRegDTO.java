package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LecturersRegDTO {
    private int id;
    private String user_id;
    private String full_name;
    private String name_with_initials;
    private String email;
    private String password;
    private Year registered_year;
    private String role;
    private String department_id;
    private Boolean is_deleted;

}
