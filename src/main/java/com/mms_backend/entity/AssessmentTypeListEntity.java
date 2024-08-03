package com.mms_backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assessment_type_list")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AssessmentTypeListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String assessment_type_name;
}
