package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "marks_approved_log")
public class Marks_approved_log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String course_id;
    private int level;
    private int semester;
    private String approved_user_id;
    @Column(name = "approved_user_level")
    private String approval_level;
    private String academic_year;
    private String date_time;
    private String department_id;
    private String signature;
}

