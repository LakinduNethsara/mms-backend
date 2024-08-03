package com.mms_backend.controller;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.CourseDTO;
import com.mms_backend.dto.CourseNameIdDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("api/courses")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class CourseController {



    @Autowired
    private CourseService courseService;

    @Autowired
    private ResponseDTO responseDTO;

    @GetMapping("getcidcnamebydls/{department_id}/{level}/{semester}")
    public ResponseEntity getCidCnameByLS(@PathVariable String department_id, @PathVariable int level, @PathVariable int semester){

        List<CourseDTO> courseDTOList = courseService.findCidCnameByLS(department_id,level, semester);
        List<CourseNameIdDTO> courseNameIdDTOs = new ArrayList<>();

        for (CourseDTO courseDTO : courseDTOList) {
            CourseNameIdDTO courseNameIdDTO = new CourseNameIdDTO();
            courseNameIdDTO.setCourse_name(courseDTO.getCourse_name());
            courseNameIdDTO.setCourse_id(courseDTO.getCourse_id());
            courseNameIdDTOs.add(courseNameIdDTO);
        }

        // Check if courseNameIdDTOs is empty after populating it
        if (courseNameIdDTOs.isEmpty()) {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setMessage("No Approved Courses");
            responseDTO.setContent(courseNameIdDTOs);
            return new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(courseNameIdDTOs, HttpStatus.OK);
        }
    }

    @GetMapping("getallcourses")
    public ResponseEntity<ResponseDTO> getAllCourses(){
        ResponseDTO allCourses = courseService.getAllCourses();
        if (allCourses.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity<>(allCourses, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(allCourses,HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/getcidcnamebyls/{level}/{semester}/{department}/{approved_level}/{academic_year}")
    public ResponseEntity getCidCnameByDLS(@PathVariable int level, @PathVariable int semester,@PathVariable String department,@PathVariable String approved_level,@PathVariable String academic_year){

        ResponseDTO responseDTO = courseService.findCidCnameByDLS(level, semester,department,approved_level,academic_year);
        if (responseDTO.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }

    @PostMapping("insertbulkcourses")
    public  ResponseEntity insertBulkCourses(@RequestBody List<CourseDTO> courseDTOS){
        ResponseDTO courseAsBulk = courseService.insertCoursesAsBulk(courseDTOS);
        if (courseAsBulk.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(courseAsBulk,HttpStatus.CREATED);

        }else{
            return new ResponseEntity(courseAsBulk,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("insertacourse")
    public  ResponseEntity insertACourse(@RequestBody CourseDTO courseDTO){
        ResponseDTO insertOneCourse = courseService.insertACourse(courseDTO);
        if (insertOneCourse.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(insertOneCourse,HttpStatus.CREATED);
        }else {
            return new ResponseEntity(insertOneCourse,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getacourse/{id}")
    public ResponseEntity getACourseById(@PathVariable int id){
        ResponseDTO getACourseById =courseService.getACourseById(id);
        if (getACourseById.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(getACourseById,HttpStatus.OK);
        }else {
            return new ResponseEntity(getACourseById,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateacourse/{id}")
    public ResponseEntity updateACourseById(@RequestBody CourseDTO courseDTO){
        ResponseDTO updateOneCourseById = courseService.updateACourseById(courseDTO);
        if (updateOneCourseById.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(updateOneCourseById,HttpStatus.OK);
        }else {
            return new ResponseEntity(updateOneCourseById,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delacourse/{id}")
    public ResponseEntity deletaCourseByIb(@PathVariable int id){
        ResponseDTO deleteOneCourseById = courseService.deleteACourseById(id);
        if (deleteOneCourseById.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(deleteOneCourseById,HttpStatus.OK);
        }else {
            return new ResponseEntity(deleteOneCourseById,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("allcoursesids")
    public ResponseEntity getAllCoursesIDs(){
        ResponseDTO responses = courseService.getAllCIDs();
        if(responses.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responses,HttpStatus.OK);
        }
        else
            return new ResponseEntity(responses,HttpStatus.NOT_FOUND);
    }

    @GetMapping("getcourseforcc/{user_name}")
    public ResponseEntity getCourseForCC(@PathVariable String user_name){
        ResponseDTO getCForCC = courseService.getApprovedCourse(user_name);
        if (getCForCC.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(getCForCC,HttpStatus.OK);
        }else return new ResponseEntity(getCForCC,HttpStatus.NOT_FOUND);

    }

    @GetMapping("/getCoursesforLectCertify/{lecturer_id}")
    public ResponseEntity getCoursesforLectCertify(@PathVariable  String lecturer_id){

        ResponseDTO responseDTO = courseService.getCoursesforLectCertify(lecturer_id);
        if (responseDTO.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("getccregcourses/{user_name}")
    public ResponseEntity getCCRegCoursesBy(@PathVariable String user_name){
        ResponseDTO getcsforcc = courseService.getAllRegCourseForCC(user_name);
        if (getcsforcc.getCode().equals(VarList.RIP_SUCCESS)) return new ResponseEntity(getcsforcc,HttpStatus.OK);
        else return new ResponseEntity(getcsforcc,HttpStatus.NOT_FOUND);
    }



}

