package com.mms_backend.controller;

import com.mms_backend.dto.LecturerCoursRegDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.LecturerCourseRegService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("api/lecturerRegCourses")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LecturerRegCoursesController
{
    @Autowired
    private LecturerCourseRegService lecturerCourseRegService;
    @PostMapping("/addLecturersToCourse")
    public ResponseEntity addLecturersAsBulk(@RequestBody List<LecturerCoursRegDTO> lecturerCourseRegDTOS)
    {
        ResponseDTO responseDTO=lecturerCourseRegService.addLecturersToCourse(lecturerCourseRegDTOS);
        if (responseDTO.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(responseDTO, HttpStatus.CREATED);
        }else {
            return new ResponseEntity(responseDTO,HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("getLecbyCID")
    public ResponseEntity getLecturersByCID(@PathVariable String course_id){
        ResponseDTO responseDTO = lecturerCourseRegService.getLecByCid(course_id);
        if (responseDTO.getCode().equals(VarList.RIP_NO_DATA_FOUND)){
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);
        }else return new ResponseEntity(responseDTO,HttpStatus.OK);
    }
}
