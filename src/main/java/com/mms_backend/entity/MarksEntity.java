package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "marks")
public class MarksEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String student_id;
    private String course_id;
    private String academic_year;
    private String level;
    private String semester;
    private String assignment_name;
    private String assignment_score;
    private String evaluation_criteria_id;

}
