package com.mms_backend.controller;


import com.mms_backend.dto.AttendanceEligibilityDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.AttendanceEligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/attendanceEligibility")
public class AttendanceEligibilityController
{
    @Autowired
    private AttendanceEligibilityService attendanceEligibilityService;

    @GetMapping("/getAttendanceEligibilityByStuIdCourseId/{course_id},{student_id}")
    public ResponseEntity getAttendanceEligibilityByStuIdCourseId(@PathVariable String course_id,@PathVariable String student_id)
    {
        AttendanceEligibilityDTO list=attendanceEligibilityService.getAttendanceEligibilityByStuIdCourseId(course_id,student_id);

        System.out.println(list);
        if(list==null)
        {
            return new ResponseEntity( HttpStatus.NOT_FOUND);
        }
        else
        {
            return  new ResponseEntity(list,HttpStatus.OK);
        }

    }

    @GetMapping("getallattendance")
    public ResponseEntity<ResponseDTO> getAllAttendance(){
        ResponseDTO allAttendance = attendanceEligibilityService.getAllAttendance();
        if (allAttendance.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity<>(allAttendance,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(allAttendance,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("insertbulkattendance")
    public ResponseEntity insertBulkAttendance(@RequestBody List<AttendanceEligibilityDTO> attendanceEligibilityDTOS){
        ResponseDTO attendanceAsBulk = attendanceEligibilityService.insertAttendancesAsBulk(attendanceEligibilityDTOS);
        if (attendanceAsBulk.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(attendanceAsBulk,HttpStatus.CREATED);
        }else {
            return new ResponseEntity(attendanceAsBulk,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("insertoneattendance")
    public ResponseEntity insertAAttendance(@RequestBody AttendanceEligibilityDTO attendanceEligibilityDTO){
        ResponseDTO insertOneAttendance = attendanceEligibilityService.insertAAttendance(attendanceEligibilityDTO);
        if (insertOneAttendance.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(insertOneAttendance,HttpStatus.CREATED);
        }else {
            return new ResponseEntity(insertOneAttendance,HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("getattendance/{id}")
    public ResponseEntity getAAttendanceById(@PathVariable int id){
        ResponseDTO getOneAttendanceById = attendanceEligibilityService.getAAttendanceById(id);
        if (getOneAttendanceById.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(getOneAttendanceById,HttpStatus.OK);
        }else {
            return new ResponseEntity(getOneAttendanceById,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateattendance/{id}")
    public ResponseEntity updateAAttendanceById(@RequestBody AttendanceEligibilityDTO attendanceEligibilityDTO){
        ResponseDTO updateOneAttendanceById = attendanceEligibilityService.updateAAttendanceById(attendanceEligibilityDTO);
        if (updateOneAttendanceById.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(updateOneAttendanceById,HttpStatus.OK);
        }else {
            return new ResponseEntity(updateOneAttendanceById,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("delattendance/{id}")
    public ResponseEntity deleteAAttendanceById(@PathVariable int id){
        ResponseDTO deleteOneAttendanceById = attendanceEligibilityService.deleteAAttendanceById(id);
        if (deleteOneAttendanceById.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(deleteOneAttendanceById,HttpStatus.OK);
        }else {
            return new ResponseEntity(deleteOneAttendanceById,HttpStatus.NOT_FOUND);
        }
    }
}
