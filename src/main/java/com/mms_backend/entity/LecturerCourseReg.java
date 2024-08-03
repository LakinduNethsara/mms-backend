package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "lecturer_course_reg")
public class LecturerCourseReg
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String user_id;

    private String course_id;

}
