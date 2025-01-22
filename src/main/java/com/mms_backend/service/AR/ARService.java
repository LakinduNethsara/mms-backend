package com.mms_backend.service.AR;
import com.mms_backend.Util.VarList;
import com.mms_backend.dto.*;
import com.mms_backend.dto.AR.*;
import com.mms_backend.dto.AR.CourseDTO;
import com.mms_backend.dto.AR.MedicalDTO;
import com.mms_backend.entity.AR.*;
import com.mms_backend.entity.GPA;
import com.mms_backend.entity.MarksEntity;
import com.mms_backend.entity.StudentRegCourses;
import com.mms_backend.entity.User;
import com.mms_backend.repository.AR.*;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class ARService {

    @Autowired
    private ARCourseRepo arCourseRepo;
    @Autowired
    private ArMarksRepo arMarksRepo;
    @Autowired
    private ARMarksApprovalLevelRepo arMarksApprovalLevelRepo;
    @Autowired
    private ARGradeRepo arGradeRepo;
    @Autowired
    private ARMedicalRepo arMedicalRepo;
    @Autowired
    private ARAcademicYearDetailsRepo arAcademicYearDetailsRepo;
    @Autowired
    private ARUserRepo arUserRepo;
    @Autowired
    private ARResultBoardRepo arResultBoardRepo;
    @Autowired
    private ARResultBoardMemberRepo arResultBoardMemberRepo;
    @Autowired
    private ARGPARepo arGPARepo;
    @Autowired
    private ARStudentRegCourses arStudentRegCourses;

    @Autowired
    private ModelMapper mp;
    @Autowired
    private ResponseDTO responseDTO;




    /* ----------------------------------------------------------------------New Update Start -------------------------------------------------------------------------------------------------------------------------------------------------------*/


    /*---------------------------------------------------------------------------------------- Service for medical table ----------------------------START-------------*/

    public List<MedicalDTO> getAllMedicalSubmissionsByYear(String academic_year){     //This method get all medical submission list related to a particular year
        List<Medical> allMedicalList = arMedicalRepo.getAllMedicalSubmissionsByYear(academic_year);
        return  mp.map(allMedicalList,new TypeToken<ArrayList<MedicalDTO>>(){}.getType());
    }

    public List<MedicalDTO> getAllMedicalSubmissions(){     //This method get all medical submission list
        List<Medical> allMedicalList = arMedicalRepo.getAllMedicalSubmissions();
        return  mp.map(allMedicalList,new TypeToken<ArrayList<MedicalDTO>>(){}.getType());
    }

    //Get medical details of selected one student for a selected exam
    public List<MedicalDTO> getSelectedStudentMedicalDetails(String student_id, String course_id, String academic_year, String exam_type){
        List<Medical> selectedStudentMedicalDetails = arMedicalRepo.getSelectedStudentMedicalDetails(student_id,course_id,academic_year,exam_type);
        return mp.map(selectedStudentMedicalDetails,new TypeToken<ArrayList<MedicalDTO>>(){}.getType());
    }

    /*---------------------------------------------------------------------------------------- Service for medical table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Service for marks table ----------------------------START-------------*/


    //Get all from marks table by providing student id , course id, academic year, and exam type
    public List<MarksDTO> getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(String student_id, String course_id, String academic_year , String exam_type){
        List<MarksEntity> marksList = arMarksRepo.getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(student_id, course_id, academic_year, exam_type);
        return mp.map(marksList,new TypeToken<ArrayList<MarksDTO>>(){}.getType());
    }
    public int updateStudentScore(UpdateABDTO updateABDTO){      //Update selected student grade with medical submissions

        return arMarksRepo.updateStudentScore(updateABDTO.getNew_score(), updateABDTO.getStudent_id(), updateABDTO.getCourse_id(), updateABDTO.getAcademic_year(),updateABDTO.getExam_type());

    }

    public boolean isABStudentAvailable(String academic_year, String semester, String level, String department_id ){        //Check whether there are any absence students in the selected department, selected level, selected semester, selected academic year for end or mid
        List<MarksEntity> abStudentList = arMarksRepo.isABStudentAvailable(academic_year, semester, level, department_id);

        return (!abStudentList.isEmpty());
    }


    public List<Object> getAllAcademicYearList(){
        return arMarksRepo.getAllAcademicYearList();
    }

    public List<Object[]> getABDetails(String approved_level){        //Get all  students records to list down from marks table having AB s for valid exams
        List<Object[]> eStarList= arMarksRepo.getABDetails(approved_level);
        return eStarList;
    }

    public List<Object[]> getABDetailsByCourseId(String course_id, String academic_year){        //Get student id and other details from marks table where grade is E* by selected course........
        List<Object[]> abList= arMarksRepo.getABDetailsByCourseId(course_id, academic_year);
        return abList;
    }


    /*---------------------------------------------------------------------------------------- Service for marks table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Service for grade table ----------------------------START-------------*/
    public void updateStudentFinalGrade(GradeDTO gradeDTO){         //Update selected student's Final grade

        if(arGradeRepo.existsById(gradeDTO.getId())) {
            arGradeRepo.save(mp.map(gradeDTO, Grade.class));
        }


    }

    public List<GradeDTO> findAllStudentMarksGrade(String course_id){          //Get all student grades of selected course module
        List<Grade> gradeList = arGradeRepo.findAllStudentGrade(course_id);
        return mp.map(gradeList,new TypeToken<ArrayList<GradeDTO>>(){}.getType());

    }

    public List<GradeDTO> findSelectedStudentMarksGrade(String course_id, String student_id){          //Get selected student grades of selected course module
        List<Grade> gradeList = arGradeRepo.findSelectedStudentGrade(course_id,student_id);
        return mp.map(gradeList,new TypeToken<ArrayList<GradeDTO>>(){}.getType());

    }


    public List<GradeDTO> getGradesForResultBoard(int level, int semester, String department_id, String academic_year){          //Get list of all the grades by selected student id
        List<Grade> gradeList = arGradeRepo.getGradesForResultBoard(level, semester, department_id, academic_year);
        return mp.map(gradeList,new TypeToken<ArrayList<GradeDTO>>(){}.getType());

    }
    /*---------------------------------------------------------------------------------------- Service for grade table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Service for Gpa table ----------------------------START-------------*/

    public List<GPADTO> getGpaListForResultBoard(String department_id, String academic_year, int level, int semester){        //Get list of all the GPAs for result board ar view
        List<GPA> gpaList= arGPARepo.getGpaListForResultBoard(department_id, academic_year, level, semester);
        return mp.map(gpaList,new TypeToken<ArrayList<GPADTO>>(){}.getType());
    }


    public void calculateSGPA(String level, String semester, String departmentId, String academicYear){         //Calculate GPA and insert SGPA to the table - Stored procedure
        arGPARepo.calculateSGPA(level, semester, departmentId, academicYear);
    }

    /*---------------------------------------------------------------------------------------- Service for Gpa table ----------------------------END-------------*/







    /*---------------------------------------------------------------------------------------- Service for course table ----------------------------START-------------*/
    public List<CourseDTO> getViewMarksCourseList(String level, String semester, String department_id , String academic_year){      //Get all course details of selected department by level and semester
        List<Course> courseList= arCourseRepo.getViewMarksCourseList(level, semester, department_id,academic_year);
        return mp.map(courseList,new TypeToken<ArrayList<CourseDTO>>(){}.getType());
    }

    public List<CourseDTO> getCourseListRemainingToAddToResultBoard(int level, int semester, String department_id, int result_board_id){      //Get all the courses not added to the result board
        List<Course> courseList= arCourseRepo.getCourseListRemainingToAddToResultBoard(level, semester, department_id, result_board_id);
        return mp.map(courseList,new TypeToken<ArrayList<CourseDTO>>(){}.getType());

    }

    public List<CourseDTO> getCourseDetailsForMarkSheet(int level, int semester, String department_id, String academic_year){      //Get all the courses not added to the result board
        List<Course> courseList= arCourseRepo.getCourseDetailsForMarkSheet(level, semester, department_id, academic_year);
        return mp.map(courseList,new TypeToken<ArrayList<CourseDTO>>(){}.getType());

    }

    /*---------------------------------------------------------------------------------------- Service for course table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Service for approve level table ----------------------------START-------------*/

    public List<MarksApprovalLevelDTO> getNotApprovedCoursesByLevelSemester(String level, String semester, String approval_level, String academic_year, String department_id){         //Get * from marks Approval level table by selected level, semester, academic year and where approval level is not equal to provided level


        List<MarksApprovalLevel> notApprovedList=arMarksApprovalLevelRepo.getNotApprovedCoursesByLevelSemester( level,semester, approval_level, academic_year, department_id);
        return  mp.map(notApprovedList,new TypeToken<ArrayList<MarksApprovalLevelDTO>>(){}.getType());

    }

    public List<MarksApprovalLevelDTO> getMarksApprovalLevelBySelectedCourseAndAcademicYear(String course_id, String academic_year ){           //Get * from marks Approval level table by selected level, semester, academic year and where approval level is not equal to provided level
        List<MarksApprovalLevel> list = arMarksApprovalLevelRepo.getMarksApprovalLevelBySelectedCourseAndAcademicYear(course_id,academic_year);
        return mp.map(list,new TypeToken<ArrayList<MarksApprovalLevelDTO>>(){}.getType());
    }


    public void updateApprovedLevelAfterResultBoard(String academic_year, String department_id, int level, int semester){         //Update approved level after result board finished
        arMarksApprovalLevelRepo.updateApprovedLevelAfterResultBoard(academic_year, department_id, level, semester);
    }

    /*---------------------------------------------------------------------------------------- Service for approve level table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Service for academic_year_details table ----------------------------START-------------*/
    public List<AcademicYearDetailsDTO> getAcademicYearDetails(){
        List<AcademicYearDetails> academicYearDetails= arAcademicYearDetailsRepo.findAll();
        return mp.map(academicYearDetails, new TypeToken<ArrayList<AcademicYearDetailsDTO>>(){}.getType());
    }



    public AcademicYearDetails updateAcademicYearDetailsByYear(int id, String current_academic_year, String current_semester) {
        // Retrieve the existing academic year details by ID
        AcademicYearDetails details = arAcademicYearDetailsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Academic year details not found"));

        // Update previous academic year if current academic year changes
        if (current_academic_year != null && !current_academic_year.equals(details.getCurrent_academic_year())) {
            System.out.println("Set previous academic year: " + details.getCurrent_academic_year());
            details.setPrevious_academic_year(details.getCurrent_academic_year());

            System.out.println("Set current academic year: " + current_academic_year);
            details.setCurrent_academic_year(current_academic_year);
        }

        // Update current semester
        if (current_semester != null) {
            System.out.println("Set current semester: " + current_semester);
            details.setCurrent_semester(current_semester);
        }

        // Save the updated details back to the database
        return arAcademicYearDetailsRepo.save(details);
//        System.out.println("all details "+details);

    }



    /*---------------------------------------------------------------------------------------- Service for academic_year_details table ----------------------------END-------------*/







    /*---------------------------------------------------------------------------------------- Service for User table ----------------------------START-------------*/

    public List<UserDTO> findAllUserDetailsBySelectedRole(String role){            //Get all user details by selected role
        List<User> HODList = arUserRepo.findAllUserDetailsBySelectedRole(role);
        return mp.map(HODList,new TypeToken<ArrayList<UserDTO>>(){}.getType());
    }


    public List<UserDTO> getAllCourseCoordinatorsBySelectedAcademicYearDepartmentLevelSemester(String academic_year, String department_id, String level, String semester){          //Get all course coordinator details by selected academic year department level and semester
        List<User> coordinatorList = arUserRepo.getAllCourseCoordinatorsBySelectedAcademicYearDepartmentLevelSemester(academic_year, department_id, level, semester);
        return mp.map(coordinatorList,new TypeToken<ArrayList<UserDTO>>(){}.getType());

    }


    /*---------------------------------------------------------------------------------------- Service for User table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Service for result board table ----------------------------START-------------*/

    public boolean isResultBoardAvailable(String department, String level, String semester, String academic_year) {            //Get result board availability
        List<ResultBoard> resultBoard= arResultBoardRepo.isResultBoardAvailable(department,level,semester,academic_year);
        return (!resultBoard.isEmpty());


    }

    public List<ResultBoardDTO> getCreatedResultBoardList(){          //Get Not started result board list
        List<ResultBoard> resultBoardList = arResultBoardRepo.getCreatedResultBoardList();
        return mp.map(resultBoardList,new TypeToken<ArrayList<ResultBoardDTO>>(){}.getType());
    }

    public List<ResultBoardDTO> getFinishedResultBoardList(){          //Get finished result board list
        List<ResultBoard> resultBoardList = arResultBoardRepo.getFinishedResultBoardList();
        return mp.map(resultBoardList,new TypeToken<ArrayList<ResultBoardDTO>>(){}.getType());
    }

    public void saveResultBoard(ResultBoardDTO resultBoardDTO){         //Save result board
        ResultBoard resultBoard = mp.map(resultBoardDTO, ResultBoard.class);
        arResultBoardRepo.save(resultBoard);
    }

    public ResultBoardDTO getResultBoardDetailsByID(int id){         //Get result board details by id

        if(arResultBoardRepo.existsById(id)) {

            Optional<ResultBoard> resultBoard= arResultBoardRepo.findById(id);
            return mp.map(resultBoard, ResultBoardDTO.class);
        }else{
            return null;
        }

    }


    public boolean deleteResultBoardById(int id){         //Delete result board
        if(arResultBoardRepo.existsById(id)) {
            arResultBoardRepo.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    public int deleteNotStartedResultBoard(int id){         //Delete result board with relevant member records
        return arResultBoardRepo.deleteNotStartedResultBoard(id);
    }


    public List<ResultBoardDTO> getCertifyPendingResultBoards(String approval_level, String status){          //Get result board details where AR can certify (Available for AR certification)
        List<ResultBoard> resultBoardList = arResultBoardRepo.getCertifyPendingResultBoards(approval_level, status);
        return mp.map(resultBoardList,new TypeToken<ArrayList<ResultBoardDTO>>(){}.getType());
    }



    /*---------------------------------------------------------------------------------------- Service for result board table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Service for result board member table ----------------------------START-------------*/

    public boolean saveResultBoardMember(ResultBoardMemberDTO resultBoardMemberDTO){         //Save result board member
        ResultBoardMember resultBoardMember = mp.map(resultBoardMemberDTO, ResultBoardMember.class);
        if(arResultBoardMemberRepo.existsById(resultBoardMember.getId())){
            return false;
        }else{
            arResultBoardMemberRepo.save(resultBoardMember);
            return true;
        }
    }


    public List<Object> getAssignedMarksSheetsByResultBoardID(int result_board_id){        //Get all assigned marks sheets by result board id
        List<Object> resultBoardMemberList = arResultBoardMemberRepo.getAssignedMarksSheetsByResultBoardID(result_board_id);
        return resultBoardMemberList;
    }

    public List<Object> getAssignedMarksSheetsByExaminerIdAndResultBoardID(int result_board_id, String course_coordinator_id){        //Get all assigned marks sheets by result board id and course coordinator id
        return arResultBoardMemberRepo.getAssignedMarksSheetsByExaminerIdAndResultBoardID(result_board_id, course_coordinator_id);

    }


    public boolean deleteResultBoardMemberById(int id){         //Delete result board member
        if(arResultBoardMemberRepo.existsById(id)) {
            arResultBoardMemberRepo.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    public int deleteAssignedMarksSheetsByResultBoardID(int result_board_id){         //Delete all assigned marks sheets by result board id
        return arResultBoardMemberRepo.deleteAssignedMarksSheetsByResultBoardID(result_board_id);
    }



    /*---------------------------------------------------------------------------------------- Service for result board member table ----------------------------END-------------*/








    /*---------------------------------------------------------------------------------------- Service for studentRegCourses table ----------------------------START-------------*/

    public List<StudentRegCoursesDTO> checkStudentRepeatStatus(String student_id, String course_id, String academic_year){        //Check whether student is repeating the course
        List<StudentRegCourses> studentRegCoursesList = arStudentRegCourses.checkStudentRepeatStatus(student_id, course_id, academic_year);
        return mp.map(studentRegCoursesList,new TypeToken<ArrayList<StudentRegCoursesDTO>>(){}.getType());
    }



    /*---------------------------------------------------------------------------------------- Service for studentRegCourses table ----------------------------END-------------*/



    /* ---------------------------------------------------------------------------------------- Controller for functionalities of UpdateABPage in frontend------------START---------*/

    public ResponseDTO checkMedicalForAB(Object studentDetails){

        ResponseDTO result = new ResponseDTO();
        Map<String,Object> studentDetailsMap = (Map<String, Object>) studentDetails;

        String student_id = (String) studentDetailsMap.get("student_id");
        String course_id = (String) studentDetailsMap.get("course_id");
        String course_name = (String) studentDetailsMap.get("course_name");
        String academic_year = (String) studentDetailsMap.get("academic_year");
        String exam_type = (String) studentDetailsMap.get("exam_type");
        String marks_table_exam_type = (String) studentDetailsMap.get("marks_table_exam_type");
        String midORend = (String) studentDetailsMap.get("midORend");
        String grade = (String) studentDetailsMap.get("grade");


        System.out.println("student_id "+student_id);
        System.out.println("course_id "+course_id);
        System.out.println("course_name "+course_name);
        System.out.println("academic_year "+academic_year);
        System.out.println("exam_type "+exam_type);
        System.out.println("marks_table_exam_type "+marks_table_exam_type);
        System.out.println("midORend "+midORend);
        System.out.println("grade "+grade);

        try{
            List<Medical> allMedicalList = arMedicalRepo.getAllMedicalSubmissionsByYear(academic_year);     //Get all medical submissions by academic year

            if(allMedicalList.isEmpty()){       //If there are no medical submissions available
                result.setCode(VarList.RIP_NO_DATA_FOUND);
                result.setMessage("Medical List is still not uploaded for the relevant academic year...");
                result.setContent(null);

            }else{

                System.out.println("Medical submissions available");
                List<Medical> selectedStudentMedicalDetails = arMedicalRepo.getSelectedStudentMedicalDetails(student_id,course_id,academic_year,midORend);        //Get selected student medical details

                if(selectedStudentMedicalDetails.isEmpty()){
                    result.setCode(VarList.RIP_SUCCESS);
                    result.setMessage("Student has not submitted a medical.");
                    //set content as newScore = 'F', color = "red"
                    result.setContent("F");
                }
            }
        }catch(Exception e){
            result.setCode(VarList.RIP_ERROR);
            result.setMessage("Error occurred while checking medical submissions...");
            result.setContent(null);
            return result;
        }


        return result;

    }


    /* ---------------------------------------------------------------------------------------- Controller for functionalities of UpdateABPage in frontend------------END---------*/







}

