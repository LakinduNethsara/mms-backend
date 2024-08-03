package com.mms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "notifications")
public class NotificationsEntity
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String receiver_id;
    private String course_id;
    private String student_id;
    private String remark;
    private String status;
}
