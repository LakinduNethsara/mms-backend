package com.mms_backend.controller;

import com.mms_backend.dto.CourseCoordinatorDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.CourseCoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/ccmanage")
public class CourseCoordinatorController {
    @Autowired
    CourseCoordinatorService courseCoordinatorService;

    @GetMapping("getallccs")
    public ResponseEntity<ResponseDTO> getAllCCs(){
        ResponseDTO allCCs = courseCoordinatorService.getAllCCs();
        if (allCCs.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity<>(allCCs, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(allCCs,HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("insertacc")
    public ResponseEntity insertCC(@RequestBody CourseCoordinatorDTO courseCoordinatorDTO){
        ResponseDTO insertOneCC = courseCoordinatorService.insertCC(courseCoordinatorDTO);
        System.out.println(courseCoordinatorDTO.getSelectedLecturerIds());
        if (insertOneCC.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(insertOneCC,HttpStatus.CREATED);
        }else {
            return new ResponseEntity(insertOneCC,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("getacc/{id}")
    public ResponseEntity getCCById(@PathVariable int id){
        ResponseDTO getCCbyID = courseCoordinatorService.getCCById(id);
        if (getCCbyID.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(getCCbyID,HttpStatus.OK);
        }else {
            return new ResponseEntity(getCCbyID,HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("updateacc/{id}")
    public ResponseEntity updateCCById(@RequestBody CourseCoordinatorDTO courseCoordinatorDTO){
        ResponseDTO updateOneCCById = courseCoordinatorService.updateCCById(courseCoordinatorDTO);
        if (updateOneCCById.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(updateOneCCById,HttpStatus.OK);
        }else {
            return new ResponseEntity(updateOneCCById,HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("delacc")
    public ResponseEntity deleteCCById(@PathVariable int id){
        ResponseDTO deleteOneCC = courseCoordinatorService.deleteCCById(id);
        if (deleteOneCC.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(deleteOneCC,HttpStatus.OK);
        }else {
            return new ResponseEntity(deleteOneCC,HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("getCCByCourse/{course_id}")
    public ResponseEntity getCCByCourse(@PathVariable String course_id)
    {
        ResponseDTO responseDTO=courseCoordinatorService.getCCbyCourse((course_id));
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }else {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("getAllCidToCourseCriteria/{user_name}")
    public ResponseEntity getAllCidToCourseCriteria(@PathVariable String user_name){
        ResponseDTO responseDTO = courseCoordinatorService.getAllCIDForCC((user_name));
        if (responseDTO.getCode().equals(VarList.RIP_SUCCESS)){
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }else {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);
        }
    }


}
