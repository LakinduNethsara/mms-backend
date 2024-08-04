package com.mms_backend.controller;

import com.mms_backend.dto.EvaluationCriteriaNameDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.EvaluationCriteriaNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/evaluationCriteriaName")
public class EvaluationCriteriaNameController {

    @Autowired
    private EvaluationCriteriaNameService evaluationCriteriaNameService;

    @Autowired
    private ResponseDTO responseDTO;

    @PostMapping("insertcriterianame")
    public ResponseEntity insertAEvCriteriaName(@RequestBody List<EvaluationCriteriaNameDTO> evaluationCriteriaNameDTOList){
        ResponseDTO evCriteriaName = evaluationCriteriaNameService.insertEvCriteriaName(evaluationCriteriaNameDTOList);
        if (evCriteriaName.getCode().equals(VarList.RIP_SUCCESS)) return new ResponseEntity(evCriteriaName, HttpStatus.CREATED);
        else return new ResponseEntity(evCriteriaName, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("getAssessmentType/{course_id}/{type}")
    public ResponseEntity getAssessmentTypes(@PathVariable String course_id, @PathVariable String type)
    {
        ResponseDTO list=evaluationCriteriaNameService.getAssessmentTypes(course_id, type);
        if(list!=null)
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND);
    }
}
