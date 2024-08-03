package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseCoordinatorDTO {
    private int id;
    private String user_id;
    private String course_id;
    private String academic_year;
    private List<String> selectedLecturerIds=new ArrayList<>();
}
