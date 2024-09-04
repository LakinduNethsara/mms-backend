package com.mms_backend.service;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.*;
import com.mms_backend.entity.AR.MarksApprovalLevel;
import com.mms_backend.entity.Assigncertifylecturer;
import com.mms_backend.entity.Marks_approved_log;
import com.mms_backend.entity.NotificationsEntity;
import com.mms_backend.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    @Autowired
    private MailServerService mailServerService;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CourseCoordinatorRepo courseCoordinatorRepo;

    @Autowired
    private NotificationsRepo notificationsRepo;

    String message=null;


    public ResponseDTO updateApprovalLevelByDepartment(Marks_approved_logDTO marksApprovedLogDTO) {
        String email = null;
        String department;
        try {
            approved_user_levelRepo.save(modelMapper.map(marksApprovedLogDTO, Marks_approved_log.class));
            approvalLevelRepo.updateApprovedLevel(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getAcademic_year(),marksApprovedLogDTO.getApproval_level(),marksApprovedLogDTO.getDepartment_id());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setMessage("Successfully updated approval level");
            responseDTO.setContent(marksApprovedLogDTO);

            try {
                MailDetailsDTO mailDetailsDTO = new MailDetailsDTO();
                message = "Marks Return Sheet of " + marksApprovedLogDTO.getCourse_id() +"-"+marksApprovedLogDTO.getDepartment_id()+" Department " +" has been sent for your approval. ";


                if(marksApprovedLogDTO.getApproval_level().equalsIgnoreCase("course_coordinator")) {
                    email = assignCertifyLecturer.getEmail(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getDepartment_id());

                    notificationsRepo.updateNotificationState(marksApprovedLogDTO.getCourse_id());

                } else if(marksApprovedLogDTO.getApproval_level().equalsIgnoreCase("lecturer")) {
                    department = courseRepo.getDepartment(marksApprovedLogDTO.getCourse_id());
                    email = userRepo.getEmail(department, "hod");
                    System.out.println(department+email);
                }
                else if(marksApprovedLogDTO.getApproval_level().equalsIgnoreCase("HOD")) {
                    email = userRepo.getEmailByRole("ar");
                    message = "Marks Return Sheet of " + marksApprovedLogDTO.getCourse_id()+"-" +marksApprovedLogDTO.getDepartment_id()+" Department " + " has been published from the department. ";
                }

                mailDetailsDTO.setMessage(message);
                mailDetailsDTO.setToMail(email);

                mailDetailsDTO.setSubject("Marks Return Sheet " + marksApprovedLogDTO.getCourse_id());

                try {
                    // Call the mail server service method
                   mailServerService.sendEmail(mailDetailsDTO);
                } catch (Exception e) {
                    System.out.println("Error sending email: " + e.getMessage());
                }


            } catch (RuntimeException e) {
                System.out.println("Error sending email: " + e.getMessage());
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setMessage("Error updating approval level: " + e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }

//            if(marksApprovedLogDTO.getApproval_level().equalsIgnoreCase("lecturer")) {
//                assignCertifyLecturer.returningResultSheet(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getDepartment_id());
//            }

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
            System.out.println("Error assigning certify lecturer");
        }

    }

    public ResponseDTO ReturnApprovalLevelByDepartment(Marks_approved_logDTO marksApprovedLogDTO) {

        try {

            approvalLevelRepo.updateApprovedLevel(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getAcademic_year(),marksApprovedLogDTO.getApproval_level(), marksApprovedLogDTO.getDepartment_id());
            approved_user_levelRepo.removeSignature(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getDepartment_id(), marksApprovedLogDTO.getAcademic_year());
            assignCertifyLecturer.returningResultSheet(marksApprovedLogDTO.getCourse_id(),marksApprovedLogDTO.getDepartment_id());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setMessage("Successfully updated approval level");
            responseDTO.setContent(marksApprovedLogDTO);


            MailDetailsDTO mailDetailsDTO = new MailDetailsDTO();

            mailDetailsDTO.setToMail(courseCoordinatorRepo.getCourseCoordinatorEmail(marksApprovedLogDTO.getCourse_id()));
            mailDetailsDTO.setSubject("Returning Marks Return Sheet - "+marksApprovedLogDTO.getCourse_id());
            // Fetch notifications from the database
            List<NotificationsEntity> notifications = notificationsRepo.getReturningReasons(marksApprovedLogDTO.getCourse_id());
            mailDetailsDTO.setNotificationsDTOList(notifications);

            // Construct the email message
            StringBuilder message = new StringBuilder("");
            message.append("The Marks Return Sheet for Course code ").append(marksApprovedLogDTO.getCourse_id()).append(" has been returned for the following corrections.");
            mailDetailsDTO.setMessage(String.valueOf(message));
            try {
                // Call the mail server service method
                mailServerService.sendEmail(mailDetailsDTO);
            } catch (Exception e) {
                System.out.println("Error sending email: " + e.getMessage());
            }


        } catch (RuntimeException e) {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setMessage("Error updating approval level: " + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return responseDTO;
    }

//
public ResponseDTO updateApprovalLevelByDeanOffice(Marks_approved_logDTO marksApprovedLogDTO) {
    try {
        // Update approval level in the database
        approvalLevelRepo.updateApprovedLevelByDean(
                marksApprovedLogDTO.getLevel(),
                marksApprovedLogDTO.getSemester(),
                marksApprovedLogDTO.getAcademic_year(),
                marksApprovedLogDTO.getDepartment_id(),
                marksApprovedLogDTO.getApproval_level()
        );

        // Save the approval log
        approved_user_levelRepo.save(modelMapper.map(marksApprovedLogDTO, Marks_approved_log.class));

        // Prepare email details
        MailDetailsDTO mailDetailsDTO = new MailDetailsDTO();
        mailDetailsDTO.setSubject("Marks Return Sheet - Level : " + marksApprovedLogDTO.getLevel() +
                " Semester : " + marksApprovedLogDTO.getSemester() +
                " Department of " + marksApprovedLogDTO.getDepartment_id() +
                " Academic Year :" + marksApprovedLogDTO.getAcademic_year());

        String email = null;
        String message = "Department of " + marksApprovedLogDTO.getDepartment_id() + ", Level " +
                marksApprovedLogDTO.getLevel() + ", Semester " +
                marksApprovedLogDTO.getSemester() + " Result Sheet has been sent for approval.";

        if (marksApprovedLogDTO.getApproval_level().equalsIgnoreCase("RB")) {
            email = userRepo.getEmailByRole("ar");
        } else if (marksApprovedLogDTO.getApproval_level().equalsIgnoreCase("AR")) {
            email = userRepo.getEmailByRole("dean");
        } else if (marksApprovedLogDTO.getApproval_level().equalsIgnoreCase("Dean")) {
            email = userRepo.getEmailByRole("ar");
            message = "Department of " + marksApprovedLogDTO.getDepartment_id() + ", Level " +
                    marksApprovedLogDTO.getLevel() + ", Semester " +
                    marksApprovedLogDTO.getSemester() + " Result Sheet has been sent for Results publishing.";
        }

        mailDetailsDTO.setMessage(message);
        mailDetailsDTO.setToMail(email);

        // Send email asynchronously
        CompletableFuture.runAsync(() -> {
            try {
                mailServerService.sendEmail(mailDetailsDTO);
                System.out.println("Email sent successfully");
            } catch (Exception e) {
                System.err.println("Error sending email: " + e.getMessage());
            }
        });

        responseDTO.setCode(VarList.RIP_SUCCESS);
        responseDTO.setMessage("Successfully updated approval level and email sent.");
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

    public ResponseDTO getSignatures(String course_id, String academic_year, String department)
    {
        try {
            List<Marks_approved_log> marksApprovedLog=approved_user_levelRepo.getSignatures(course_id,academic_year,department);
            if(marksApprovedLog!=null)
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setMessage("successfuly get");
                responseDTO.setContent(modelMapper.map(marksApprovedLog,new TypeToken<ArrayList<Marks_approved_logDTO>>(){}.getType()));
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

    public String getApprovalLevel(String course_id,String department_id)
    {
        MarksApprovalLevel marksApprovalLevel=approvalLevelRepo.getApprovalLevel(course_id,department_id);
        return marksApprovalLevel.getApproval_level();
    }

    public ResponseDTO getMarkSheetsForHOD(String department_id)
    {
        List<Object[]> list=courseRepo.getHODApprovalLevelCourse(department_id);
        if(!list.isEmpty())
        {
            responseDTO.setMessage("Found");
            responseDTO.setContent(list);
            responseDTO.setCode(VarList.RIP_SUCCESS);
        }
        else
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setMessage("NOT FOUND");
            responseDTO.setContent(null);
        }
        return responseDTO;
    }

}
