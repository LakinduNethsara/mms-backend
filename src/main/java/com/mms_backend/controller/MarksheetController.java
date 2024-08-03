package com.mms_backend.controller;

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

    @GetMapping("getMarks/{course_id}")
    public List<StudentData> getMarks(@PathVariable String course_id )
    {
        return markSheetService.getData(course_id);
    }

    @GetMapping("getStudentMarks/{course_id}")
    public List<StudentData> getStudentMarks(@PathVariable String course_id )
    {
        return markSheetService.getData(course_id);
    }

    @PutMapping("updateMarks")
    public void updateStudentMarks(@RequestBody StudentData studentData)
    {
        System.out.println("Came here 1");
        markSheetService.updateEndMarks(studentData);
    }
}
