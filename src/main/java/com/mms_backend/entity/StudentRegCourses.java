package com.mms_backend.entity;

import com.mms_backend.service.StudentCourseId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "studentregcourses")
@IdClass(StudentCourseId.class)
public class StudentRegCourses
{
    @Id
    private String student_id;
    @Id
    private String course_id;

    private String academic_year;

    private int is_repeat;
}
