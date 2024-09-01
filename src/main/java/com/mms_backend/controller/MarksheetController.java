package com.mms_backend.controller;

import com.mms_backend.dto.UpdateMarksDTO;
import com.mms_backend.entity.Marks_edit_log;
import com.mms_backend.entity.StudentData;
import com.mms_backend.service.MarkSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("api/marksReturnSheet")
public class MarksheetController
{
    @Autowired
    MarkSheetService markSheetService;

    @GetMapping("getMarks/{course_id}/{type}/{academicYear}/{department}")
    public List<StudentData> getMarks(@PathVariable String course_id ,@PathVariable int type,@PathVariable String academicYear,@PathVariable String department)
    {
        return markSheetService.getData(course_id,type,academicYear,department);
    }

    @GetMapping("getStudentMarks/{course_id}/{type}/{academicYear}/{department}")
    public List<StudentData> getStudentMarks(@PathVariable String course_id ,@PathVariable int type,@PathVariable String academicYear,@PathVariable String department)
    {
        return markSheetService.getData(course_id,type,academicYear,department);
    }

    @PutMapping("updateMarks")
    public void updateStudentMarks(@RequestBody UpdateMarksDTO updateMarksDTO)
    {
        markSheetService.updateEndMarks(updateMarksDTO.getStudentData(),updateMarksDTO.getMarksEditLogDTO());
    }


}
