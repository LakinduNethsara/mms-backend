package com.mms_backend.controller;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.CourseRelatedDeptDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.service.CourseRelatedDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("api/coursedept")
public class CourseRelatedDeptController {

    @Autowired
    private CourseRelatedDeptService courseRelatedDeptService;

    @Autowired
    private ResponseDTO responseDTO;

    @PostMapping("insertacoursetocrdept")
    public ResponseEntity insertACourseToCRDept(@RequestBody List<CourseRelatedDeptDTO> courseRelatedDeptDTO) {
        ResponseDTO insertOneCourse = courseRelatedDeptService.insertACourseToCRDept(courseRelatedDeptDTO);
        if (insertOneCourse.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(insertOneCourse, HttpStatus.OK);
        }else{
            return new ResponseEntity(insertOneCourse, HttpStatus.BAD_REQUEST);
        }
    }

}
