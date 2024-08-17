package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarksEditLogDTO
{
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
