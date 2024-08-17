package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="marks_edit_log")
public class Marks_edit_log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String user_id;
    private String student_id;
    private String course_id;
    private String type;
    private String pre_mark;
    private String current_mark;
    private Date date_time;
    private String reason;
}
