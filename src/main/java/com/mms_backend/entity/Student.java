package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Entity
@Table(name="students")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String student_id;

    private String full_name;

    private String name_with_initials;

    private String user_name;
    private String email;
    private String password;
    private Year registered_year;
    private  String department_id;

    public Student(String student_id) {
        this.student_id = student_id;
    }
}
