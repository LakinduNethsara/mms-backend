package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="assigncertifylecturer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Assigncertifylecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String lecturer_id;

    private String course_id;
}
