package com.mms_backend.controller;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.service.ResultBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/results_board")
public class ResultBoardController
{
    @Autowired
    private ResultBoardService resultBoardService;

    @GetMapping("getAvailableResultsBoardsforDeanCC/{department_id}")
    public ResponseEntity getAvailableResultsBoardsforDeanCC(@PathVariable String department_id)
    {
        ResponseDTO responseDTO=resultBoardService.getAvailableResultBoardforHODandCC(department_id);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("getAssignedResultSheet/{user_id}")
    public ResponseEntity getAssignedResultSheetsforRB(@PathVariable String user_id)
    {
        ResponseDTO responseDTO=resultBoardService.getAssignedResultSheetsforDeanCC(user_id);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND );
        }
    }
}
