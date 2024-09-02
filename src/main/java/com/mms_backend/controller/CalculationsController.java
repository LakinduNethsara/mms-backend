package com.mms_backend.controller;


import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.CalculationsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/marksCalculations")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalculationsController
{
    @Autowired
    private CalculationsService calculationsService;

//    @GetMapping("/getMarksCalculation/{course_id}/{academic_year}")
//
//    public ResponseEntity getMarkCalculations(@PathVariable String course_id,@PathVariable String academic_year)
//    {
//        ResponseDTO list=calculationsService.getMarksCalculations(course_id,academic_year);
//        if(list.getCode().equals(VarList.RIP_NO_DATA_FOUND))
//        {
//            return new ResponseEntity(list,HttpStatus.NOT_FOUND);
//        }
//        else
//       return new ResponseEntity(list.getContent(),HttpStatus.OK);
//    }
//
//    @GetMapping("/getMarksCalculationByStuID/{course_id}/{student_id}")
//    public ResponseEntity getMarkCalculations(@PathVariable String course_id,@PathVariable String student_id)
//    {
//        ResponseDTO list= calculationsService.getMarksCalculations(course_id,student_id);
//        if(list.getCode().equals(VarList.RIP_NO_DATA_FOUND))
//        {
//            return new ResponseEntity(list,HttpStatus.NOT_FOUND);
//        }
//        else
//            return new ResponseEntity(list.getContent(),HttpStatus.OK);
//    }

    @GetMapping("/isCalculationDetailsAvailable/{course_id}/{academic_year}")
    public boolean isCalculationDetailsAvailable(@PathVariable String course_id, @PathVariable String academic_year){
        return calculationsService.isCalculationDetailsAvailable(course_id,academic_year);
    }



}
