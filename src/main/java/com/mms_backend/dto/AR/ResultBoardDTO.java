package com.mms_backend.dto.AR;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultBoardDTO {
    private int id;
    private String department;
    private int level;
    private int semester;
    private String academic_year;
    private String status;
    private String created_date_time;
    private String conducted_date_time;
}
