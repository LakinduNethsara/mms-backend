package com.mms_backend.entity.AR;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="courses_related_departments")
public class ARCourseRelatedDepartments {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String course_id;
    private String department_id;
    private String credit;

}
