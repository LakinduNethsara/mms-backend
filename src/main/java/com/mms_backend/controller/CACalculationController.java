package com.mms_backend.controller;

import com.mms_backend.service.CACalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("api/ca")
public class CACalculationController {

    @Autowired
    private CACalculationService caCalculationService;

    @GetMapping("/calculate/{course_id}")
    public void calculateCA(@PathVariable String course_id) {
        caCalculationService.calculateCA(course_id);
    }

}
