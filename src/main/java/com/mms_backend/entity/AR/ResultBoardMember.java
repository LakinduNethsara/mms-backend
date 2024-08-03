package com.mms_backend.entity.AR;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "result_board_member")

public class ResultBoardMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String course_coordinator_id;
    private String course_id;
    private int result_board_id;
    private String assigned_date_time;
}
