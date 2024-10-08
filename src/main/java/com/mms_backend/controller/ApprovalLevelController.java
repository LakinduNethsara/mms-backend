package com.mms_backend.controller;


import com.mms_backend.Util.VarList;
import com.mms_backend.dto.AssigncertifylecturerDTO;
import com.mms_backend.dto.Marks_approved_logDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.service.ApprovalLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/approvalLevel")
public class ApprovalLevelController
{
    @Autowired
    ApprovalLevelService approvalLevelService;
    @PostMapping("/updateApprovalLevel")
    public ResponseEntity updateApprovalLevel(@RequestBody Marks_approved_logDTO marksApprovedLogDTO)
    {
        ResponseDTO responseDTO=approvalLevelService.updateApprovalLevelByDepartment(marksApprovedLogDTO);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND );
        }
    }

    @PostMapping("/assignCertifyLecturer")
    public void assignCertifyLecturer(@RequestBody AssigncertifylecturerDTO assigncertifylecturerDTO)
    {

        approvalLevelService.assignCertifyLecturer(assigncertifylecturerDTO);
    }

    @PostMapping("/return")
    public ResponseEntity ReturnApprovalLevel(@RequestBody  Marks_approved_logDTO marksApprovedLogDTO)
    {
        ResponseDTO responseDTO=approvalLevelService.ReturnApprovalLevelByDepartment(marksApprovedLogDTO);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND );
        }
    }

    @PostMapping("updateApprovalLevelByDean")
    public ResponseEntity updateApprovalLevelByDeanOffice(@RequestBody Marks_approved_logDTO marksApprovedLogDTO)
    {
        ResponseDTO responseDTO=approvalLevelService.updateApprovalLevelByDeanOffice(marksApprovedLogDTO);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("/getSignature/{course_id}/{approval_level}/{academic_year}")
    public ResponseEntity getSignature(@PathVariable String course_id,@PathVariable String approval_level,@PathVariable String academic_year)
    {
        ResponseDTO responseDTO=approvalLevelService.getSignature(course_id,approval_level,academic_year);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND );
        }
    }
    @GetMapping("/getSignatures/{course_id}/{academic_year}/{department}")
    public ResponseEntity getSignatures(@PathVariable String course_id,@PathVariable String academic_year,@PathVariable String department)
    {
        ResponseDTO responseDTO=approvalLevelService.getSignatures(course_id,academic_year,department);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("/getSignature/{level}/{semester}/{department_id}/{approval_level}/{academic_year}")
    public ResponseEntity getSignature(@PathVariable int level,@PathVariable int semester,@PathVariable String department_id,@PathVariable String approval_level,@PathVariable String academic_year)
    {
        ResponseDTO responseDTO=approvalLevelService.getSignature(level,semester,department_id,approval_level,academic_year);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(responseDTO,HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("/getApprovedLevel/{course_id}/{department_id}")
    public ResponseEntity getSignature(@PathVariable String course_id,@PathVariable String department_id)
    {
        String approvalLevel=approvalLevelService.getApprovalLevel(course_id,department_id);

            return new ResponseEntity(approvalLevel,HttpStatus.OK);


    }

    @GetMapping("/getMarksReturnSheetsForHODCertify/{department_id}")
    public ResponseEntity getMarksReturnSheetsForHODCertify(@PathVariable String department_id)
    {
        ResponseDTO response=approvalLevelService.getMarkSheetsForHOD(department_id);

        if(response.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(response,HttpStatus.OK);
        }
        else if(response.getCode().equals(VarList.RIP_NO_DATA_FOUND))
        {
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

