package com.mms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationsDTO
{
    private int id;
    private String receiver_id;
    private String course_id;
    private String student_id;
    private String remark;
    private String status;


}
