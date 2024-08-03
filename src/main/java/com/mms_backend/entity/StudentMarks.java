package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "grade")
public class StudentMarks
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String student_id;

    private String course_id;

    private String level;

    private String semester;

    private String total_ca_mark;

    private String ca_eligibility;

    private String total_final_mark;

    private String total_rounded_mark;

    private String grade;

    private String gpv;





}
