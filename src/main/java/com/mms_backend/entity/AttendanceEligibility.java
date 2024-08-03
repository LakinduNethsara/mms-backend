package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="attendance_eligibility")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendanceEligibility
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String student_id;

    private String course_id;

    private double percentage;

    private String eligibility;

}
