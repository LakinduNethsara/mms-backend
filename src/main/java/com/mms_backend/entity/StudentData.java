package com.mms_backend.entity;

import com.mms_backend.dto.ObjectDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
public class StudentData
{
    private String student_id;

    private String student_name;

    private String course_id;

    private List<ObjectDTO> ca;

    private String total_ca_mark;

    private List<ObjectDTO> end;

    private String total_final_marks;

    private String total_rounded_marks;

    private String grade;

    private String gpv;

    private String ca_eligibility;
}
