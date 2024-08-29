package com.mms_backend.controller;

import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.dto.StudentRegCoursesDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.MarkSheetService;
import com.mms_backend.service.StudentRegCoursesServices;
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
@RequestMapping("api/studentRegCourses")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentRegistedCourseController
{
    @Autowired
    private StudentRegCoursesServices studentRegCoursesServices;

    @Autowired
    MarkSheetService markSheetService;


    @GetMapping("/getStudentsByCourse/{course_id}")
    public ResponseEntity getStudentsByCourse(@PathVariable String course_id)
    {
        List<String> studentList=new ArrayList<>();

        ResponseDTO responseDTO=studentRegCoursesServices.getStudentsByCourseCode(course_id);

        List<StudentRegCoursesDTO> list= (List<StudentRegCoursesDTO>) responseDTO.getContent();



        if(responseDTO.getCode()== VarList.RIP_NO_DATA_FOUND)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            for (StudentRegCoursesDTO studentRegCoursesDTO : list)
            {
                studentList.add(studentRegCoursesDTO.getStudent_id());
            }
            return new ResponseEntity(studentList,HttpStatus.OK);

        }




    }



    @GetMapping("getallRegisteredStudents")
    public ResponseEntity<ResponseDTO> getallRegisteredStudents(){
        ResponseDTO allStudents = studentRegCoursesServices.getStudents();
        if (allStudents.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity<>(allStudents, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(allStudents,HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("insert_allRegisteredStudents")
    public ResponseEntity insert_allRegisteredStudents(@RequestBody List<StudentRegCoursesDTO> list){
        ResponseDTO AllStudents = studentRegCoursesServices.insertAllStudents(list);
        if (AllStudents.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(AllStudents,HttpStatus.CREATED);

        }else{
            return new ResponseEntity(AllStudents,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getMarksNotEnteredStudent/{course_id}")
    public ResponseEntity getMarksNotEnteredStudent(@PathVariable String course_id)
    {
        ResponseDTO responseDTO=studentRegCoursesServices.getMarksNotEnteredStudents(course_id);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("getallregstudents/{course_id},{academic_year}")
    public ResponseEntity getAllRegStudents(@PathVariable String course_id,@PathVariable String academic_year)
    {
        ResponseDTO responseDTO=studentRegCoursesServices.getAllRegStudentsService(course_id,academic_year);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("getregStudentsbycidanday/{course_id},{academic_year}")
    public ResponseEntity getRegStudentsByCIDAndAY(@PathVariable String course_id,@PathVariable String academic_year)
    {
        ResponseDTO responseDTO=studentRegCoursesServices.getAllStudentsByCID(course_id,academic_year);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity<>(responseDTO,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(responseDTO,HttpStatus.NOT_FOUND);
        }
    }



}
