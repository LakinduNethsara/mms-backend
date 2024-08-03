package com.mms_backend.dto.AR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultBoardMemberDTO {
    private int id;
    private String course_coordinator_id;
    private String course_id;
    private int result_board_id;
    private String assigned_date_time;

}
