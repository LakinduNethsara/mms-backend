package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="finalmarksselection")
public class FinalMarksselection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "course_id", length = 100, nullable = false)
    private String courseId;

    @Column(name = "selected", length = 45, nullable = false)
    private String selected;

    @Column(name = "evaluation_criteria_id", length = 45, nullable = false)
    private String evaluationCriteriaId;

    @Column(name = "assessment_type", length = 45, nullable = false)
    private String assessmentType;

    @Column(name = "academic_year", length = 45, nullable = false)
    private String academicYear;
}
