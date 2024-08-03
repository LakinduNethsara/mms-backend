package com.mms_backend.entity.AR;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "academic_year_details")
public class AcademicYearDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;
    private String previous_academic_year;
    private String current_academic_year;
    private String current_semester;
}
