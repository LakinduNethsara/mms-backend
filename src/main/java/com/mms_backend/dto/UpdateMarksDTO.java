package com.mms_backend.dto;

import com.mms_backend.entity.StudentData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMarksDTO
{
    private StudentData studentData;
    private MarksEditLogDTO marksEditLogDTO;
}
