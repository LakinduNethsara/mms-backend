package com.mms_backend.controller;

import com.mms_backend.dto.LecturersRegDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.LecturersRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("api/lecreg")
public class LecturersRegController {

    @Autowired
    LecturersRegService lecturersRegService;

    @PostMapping("savelecdetails")
    public ResponseEntity saveLecturerDetails(@RequestBody LecturersRegDTO lecturerRegDetails){
        ResponseDTO response=lecturersRegService.saveLecturerDetails(lecturerRegDetails);
        if(response.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);

    }

    @GetMapping("get/getAllLecurerDetails/{department_id}")
    public ResponseEntity getAllLecturerDetails(@PathVariable String department_id){
        ResponseDTO response=lecturersRegService.getAllLecturers(department_id);
        if(response.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
    }

    @GetMapping("get/allusersdetails")
    public ResponseEntity getAllLecDetails(){
        ResponseDTO response=lecturersRegService.getAllUsersData();
        if(response.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
    }

    @GetMapping("get/alllecturersdetails")
    public ResponseEntity getAllLecturerDetails(){
        ResponseDTO response=lecturersRegService.getAllLecturersData();
        if(response.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
    }


    @PutMapping("edit/alecdetails")
    public ResponseEntity editLecturerDetails(@RequestBody LecturersRegDTO lecturersRegDTO)
    {
        ResponseDTO response=lecturersRegService.editLecturerDetails(lecturersRegDTO);
        if(response.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("delete/deleteById/{id}")
    public ResponseEntity deleteLecById(@PathVariable int id)
    {
        ResponseDTO response=lecturersRegService.deleteLecByID(id);
        if(response.getCode().equalsIgnoreCase(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
    }

    @GetMapping("allLecids")
    public ResponseEntity getAllLecIds(){
        ResponseDTO response = lecturersRegService.getAllLecId();
        if(response.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
    }

    @PostMapping("insertbulkusersdetails")
    public ResponseEntity saveAllUsersDetails(@RequestBody List<LecturersRegDTO> lecturersRegDTOS){
        ResponseDTO UsersDAsBulk = lecturersRegService.insertALLUsersDetailsAsBulk(lecturersRegDTOS);
        if (UsersDAsBulk.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(UsersDAsBulk,HttpStatus.CREATED);

        }else{
            return new ResponseEntity(UsersDAsBulk,HttpStatus.BAD_REQUEST);
        }
    }
}
