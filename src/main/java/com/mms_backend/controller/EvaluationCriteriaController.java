package com.mms_backend.controller;


import com.mms_backend.dto.EvaluationCriteriaDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.EvaluationCriteriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/evaluationCriteria")
public class EvaluationCriteriaController
{
    @Autowired
    private EvaluationCriteriaService evaluationCriteriaService;

    @Autowired
    private ResponseDTO responseDTO;

    @GetMapping("/getCriteria/{course_id}")
    public ResponseEntity getEvaluationCriteria(@PathVariable String course_id)
    {
        responseDTO=evaluationCriteriaService.getEvaluationCriteria(course_id);
        if(responseDTO.getCode()== VarList.RIP_NO_DATA_FOUND)
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(responseDTO.getContent(),HttpStatus.OK);
    }

    @PostMapping("insertcriteria")
    public ResponseEntity insertAEvCriteria(@RequestBody List<EvaluationCriteriaDTO> evaluationCriteriaDTOList){
        ResponseDTO evCriteria = evaluationCriteriaService.insertEvCriteria(evaluationCriteriaDTOList);
        if (evCriteria.getCode().equals(VarList.RIP_SUCCESS)) return new ResponseEntity(evCriteria,HttpStatus.CREATED);
        else return new ResponseEntity(evCriteria,HttpStatus.BAD_REQUEST);

    }
}
