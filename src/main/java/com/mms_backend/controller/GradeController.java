package com.mms_backend.controller;

import com.mms_backend.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/grade")
public class GradeController {
    @Autowired
    GradeService gradeService;

    @GetMapping("/calculateRoundedMark/{course_id}/{academic_year}")
    public void calculateRoundedMark(@PathVariable String course_id,@PathVariable String academic_year) {
        gradeService.calculateRoundedMark(course_id,academic_year);
    }

    @GetMapping("/isFinalized/{course_id}/{academic_year}")
    public boolean isFinalized(@PathVariable String course_id,@PathVariable String academic_year) {
        return gradeService.isFinalized(course_id,academic_year);
    }

}
