package com.mms_backend.entity.AR;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "grade")
public class Grade {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String student_id;
    private String course_id;
    private String level;
    private String semester;
    private String total_ca_mark;
    private String ca_eligibility;
    private String overall_eligibility;
    private String total_final_mark;
    private String total_rounded_mark;
    private String grade;
    private String gpv;

}
