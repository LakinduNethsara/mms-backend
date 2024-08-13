package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "course")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int Id;
    private String course_id;
    private String course_name;
    private int hours;
    private String type;
    private String department_id;
    private int level;
    private int semester;



}