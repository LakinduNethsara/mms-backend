package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "marks_range_of_grade")
public class MarksRangeOfGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String course_id;

    private String grade;

    private double margin_of_grade;

    private String academic_year;

    private String user_id;

    private Date datetime;


}

