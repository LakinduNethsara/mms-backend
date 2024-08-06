package com.mms_backend.service;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.AssigncertifylecturerDTO;
import com.mms_backend.dto.Marks_approved_logDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.entity.AR.MarksApprovalLevel;
import com.mms_backend.entity.Assigncertifylecturer;
import com.mms_backend.entity.Marks_approved_log;
import com.mms_backend.repository.ApprovalLevelRepo;
import com.mms_backend.repository.Approved_user_levelRepo;
import com.mms_backend.repository.AssignCertifyLecturer;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Transactional
public class ApprovalLevelService {

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ApprovalLevelRepo approvalLevelRepo;

    @Autowired
    private Approved_user_levelRepo approved_user_levelRepo;

    @Autowired
    private AssignCertifyLecturer assignCertifyLecturer;




    public ResponseDTO updateApprovalLevelByDepartment(Marks_approved_logDTO marksApprovedLogDTO) {

        try {

            approved_user_levelRepo.save(modelMapper.map(marksApprovedLogDTO, Marks_approved_log.class));
            approvalLevelRepo.updateApprovedLevel(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getAcademic_year(),marksApprovedLogDTO.getApproval_level());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setMessage("Successfully updated approval level");
            responseDTO.setContent(marksApprovedLogDTO);
        } catch (RuntimeException e) {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setMessage("Error updating approval level: " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return responseDTO;
    }

    public void assignCertifyLecturer(AssigncertifylecturerDTO assigncertifylecturer)
    {
        try
        {
            assignCertifyLecturer.save(modelMapper.map(assigncertifylecturer, Assigncertifylecturer.class));
        }catch (Exception e)
        {
            System.out.println("Error assigning ertify lecturer");
        }

    }

    public ResponseDTO ReturnApprovalLevelByDepartment(Marks_approved_logDTO marksApprovedLogDTO) {

        try {

            approvalLevelRepo.updateApprovedLevel(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getAcademic_year(),marksApprovedLogDTO.getApproval_level());
            approved_user_levelRepo.removeSignature(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getDepartment_id(), marksApprovedLogDTO.getAcademic_year());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setMessage("Successfully updated approval level");
            responseDTO.setContent(marksApprovedLogDTO);
        } catch (RuntimeException e) {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setMessage("Error updating approval level: " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return responseDTO;
    }

    public ResponseDTO updateApprovalLevelByDeanOffice(Marks_approved_logDTO marksApprovedLogDTO) {
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        try {
            System.out.print(marksApprovedLogDTO.getLevel()+""+marksApprovedLogDTO.getSemester()+""+marksApprovedLogDTO.getDepartment_id()+""+marksApprovedLogDTO.getApproval_level()+""+marksApprovedLogDTO.getAcademic_year());
            approvalLevelRepo.updateApprovedLevelByDean(marksApprovedLogDTO.getLevel(),marksApprovedLogDTO.getSemester(),marksApprovedLogDTO.getAcademic_year(),marksApprovedLogDTO.getDepartment_id(),marksApprovedLogDTO.getApproval_level());
            approved_user_levelRepo.save(modelMapper.map(marksApprovedLogDTO,Marks_approved_log.class));
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setMessage("Successfully updated approval level");
            responseDTO.setContent(null);
        } catch (RuntimeException e) {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setMessage("Error updating approval level: " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return responseDTO;
    }

    public ResponseDTO getSignature( String course_id, String approval_level,String academic_year)
    {
        try {
            Marks_approved_log marksApprovedLog=approved_user_levelRepo.getSignature(course_id,approval_level,academic_year);
            if(!marksApprovedLog.equals(null))
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setMessage("successfuly get");
                responseDTO.setContent(modelMapper.map(marksApprovedLog,Marks_approved_logDTO.class));
            }
            else
            {
                responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
                responseDTO.setMessage("no signature");
                responseDTO.setContent(null);
            }

        } catch (RuntimeException e) {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return responseDTO;
    }

    public ResponseDTO getSignature(int level,int semester,String department_id, String approval_level,String academic_year)
    {
        try {
            Marks_approved_log marksApprovedLog=approved_user_levelRepo.getSignature(level,semester,department_id,approval_level,academic_year);
            if(!marksApprovedLog.equals(null))
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setMessage("successfuly get");
                responseDTO.setContent(modelMapper.map(marksApprovedLog,Marks_approved_logDTO.class));
            }
            else
            {
                responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
                responseDTO.setMessage("no signature");
                responseDTO.setContent(null);
            }

        } catch (RuntimeException e) {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setMessage(e.getMessage());
            responseDTO.setContent(null);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return responseDTO;
    }

    public String getApprovalLevel(String course_id)
    {
        MarksApprovalLevel marksApprovalLevel=approvalLevelRepo.getApprovalLevel(course_id);
        return marksApprovalLevel.getApproval_level();
    }

}
