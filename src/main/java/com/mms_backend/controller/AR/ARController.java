package com.mms_backend.controller.AR;

import com.mms_backend.dto.AR.*;
import com.mms_backend.dto.GPADTO;
import com.mms_backend.dto.MarksDTO;
import com.mms_backend.dto.StudentRegCoursesDTO;
import com.mms_backend.dto.UserDTO;
import com.mms_backend.entity.StudentRegCourses;
import com.mms_backend.service.AR.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/AssistantRegistrar")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ARController {
    @Autowired
    private ARService arService;


    /*---------------------------------------------------------------------------------------- Controller for course table ----------------------------START-------------*/
    @GetMapping("/getViewMarksCourseList/{level}/{semester}/{department_id}")           //Get all course details of selected department by level and semester
    public List<CourseDTO> getViewMarksCourseList (@PathVariable String level, @PathVariable String semester, @PathVariable String department_id){

        return arService.getViewMarksCourseList(level, semester,department_id);


        /*Usage
            CourseSelection
         */
    }

    @GetMapping("/getCourseListRemainingToAddToResultBoard/{level}/{semester}/{department_id}/{result_board_id}")           //Get all course details of selected department by level and semester
    public List<CourseDTO> getCourseListRemainingToAddToResultBoard (@PathVariable int level, @PathVariable int semester, @PathVariable String department_id, @PathVariable int result_board_id) {

        return arService.getCourseListRemainingToAddToResultBoard(level, semester, department_id, result_board_id);


        /*Usage

            ResultsBoardMarksSheetAssign

         */

    }

    @GetMapping("/getCourseDetailsForMarkSheet/{level}/{semester}/{department_id}/{academic_year}")           //Get all course details of selected department by level and semester
    public List<CourseDTO> getCourseDetailsForMarkSheet (@PathVariable int level, @PathVariable int semester, @PathVariable String department_id, @PathVariable String academic_year) {

        return arService.getCourseDetailsForMarkSheet(level, semester, department_id, academic_year);


        /*Usage
            ARJoinResultBoard
         */
    }
    /*---------------------------------------------------------------------------------------- Controller for course table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Controller for medical table ----------------------------START------------*/
    @GetMapping("/getAllMedicalSubmissionsByYear/{academic_year}")    //controller to get all medical list by selected academic year
    public List<MedicalDTO> getAllMedicalSubmissionsByYear(@PathVariable String academic_year){
        return arService.getAllMedicalSubmissionsByYear(academic_year);


        /*Usage
            UpdateABPage
            ViewMedicalPage
         */
    }

    @GetMapping("/getAllMedicalSubmissions")    //controller to get all medical list by selected academic year
    public List<MedicalDTO> getAllMedicalSubmissions(){
        return arService.getAllMedicalSubmissions();



        /*Usage
            ViewMedicalPage
         */
    }

    @GetMapping("/getSelectedStudentMedicalDetails/{student_id}/{course_id}/{academic_year}/{exam_type}")   //Controller to get selected student's medical details for selected exam
    public List<MedicalDTO> getSelectedStudentMedicalDetails(@PathVariable String student_id, @PathVariable String course_id, @PathVariable String academic_year, @PathVariable String exam_type){
        return arService.getSelectedStudentMedicalDetails(student_id, course_id, academic_year, exam_type);


        /*Usage
            UpdateABPage
         */
    }


    /*---------------------------------------------------------------------------------------- Controller for medical table ----------------------------END-------------*/







    /*---------------------------------------------------------------------------------------- Controller for marks table ----------------------------START-------------*/


    @GetMapping("/getABDetails/{approved_level}")      //Get all  students records to list down from marks table having AB s for valid exams
    public List<Object[]> getABDetails(@PathVariable String approved_level){
        return arService.getABDetails(approved_level);


        /*Usage
        ABListPage

         */
    }

    @GetMapping("/getABDetailsByCourseId/{course_id}")      //Get student id and other details from marks table where grade is E*        This is for view marks table to identify is there E* for the subject
    public List<Object[]> getABDetailsByCourseId(@PathVariable String course_id){
        return arService.getABDetailsByCourseId(course_id);

        /*Usage
            ViewMarksTableValidations
         */
    }

    @GetMapping("/getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear/{student_id}/{course_id}/{academic_year}/{exam_type}")                 //Get all from marks table by providing student id , course id, academic year, and exam type
    public List<MarksDTO> getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(@PathVariable String student_id, @PathVariable String course_id, @PathVariable String academic_year, @PathVariable String exam_type){
        return arService.getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(student_id, course_id, academic_year, exam_type);


        /*Usage
            UpdateABPage
         */
    }

    @PutMapping("/updateStudentScore")      //Update selected student grade with medical submissions
    public int updateStudentScore(@RequestBody UpdateABDTO updateEStarDTO){
        return arService.updateStudentScore(updateEStarDTO);

        /*Usage
            UpdateABPage
         */
    }

    @GetMapping("/isABStudentAvailable/{academic_year}/{semester}/{level}/{department_id}")      //Check whether there are any absence students in the selected department, selected level, selected semester, selected academic year for end or mid
    public boolean isABStudentAvailable(@PathVariable String academic_year, @PathVariable String semester, @PathVariable String level, @PathVariable String department_id){
        boolean abStudentList= arService.isABStudentAvailable(academic_year, semester, level, department_id);
        return abStudentList;

        /*Usage
            CreateResultsBoard
         */
    }



    /*---------------------------------------------------------------------------------------- Controller for marks table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Controller for grade table ----------------------------START-------------*/
    @PutMapping("/updateStudentFinalGrade")     //Update selected student's Final grade to WH
    public void updateStudentFinalGrade(@RequestBody GradeDTO gradeDTO){
        arService.updateStudentFinalGrade(gradeDTO);



        /*Usage
            UpdateABPage
         */
    }

    @GetMapping("/findAllStudentsGrade/{course_id}")     //Get all student grades of selected course module
    public List<GradeDTO> findAllStudentGrade(@PathVariable String course_id){
        return arService.findAllStudentMarksGrade(course_id);

        /*Usage
            ViewMarksTable
         */
    }

    @GetMapping("/findSelectedStudentGrade/{course_id}/{student_id}")     //Get selected student grades of selected course module
    public List<GradeDTO> findSelectedStudentGrade(@PathVariable String course_id, @PathVariable String student_id){
        return arService.findSelectedStudentMarksGrade(course_id,student_id);


        /*Usage
            UpdateABPage
            ViewMarksTable
         */
    }


    @GetMapping("/getGradesForResultBoard/{level}/{semester}/{department_id}/{academic_year}")     //Get list of all the grades by selected student id
    public List<GradeDTO> getGradesForResultBoard(@PathVariable int level, @PathVariable int semester, @PathVariable String department_id, @PathVariable String academic_year){
        return arService.getGradesForResultBoard(level, semester, department_id, academic_year);

        /*Usage
            CreateResultBoard
         */
    }

    /*---------------------------------------------------------------------------------------- Controller for grade table ----------------------------END-------------*/







    /*---------------------------------------------------------------------------------------- Controller for Gpa table ----------------------------START-------------*/

    @GetMapping("/getGpaListForResultBoard/{department_id}/{academic_year}/{level}/{semester}")     //Get list of all the GPAs for result board ar view
    public List<GPADTO> getGpaListForResultBoard(@PathVariable String department_id, @PathVariable String academic_year, @PathVariable int level, @PathVariable int semester){
        return arService.getGpaListForResultBoard(department_id, academic_year, level, semester);

        /*Usage
            CreateResultBoard
         */
    }

    @PostMapping("/calculateSGPA/{level}/{semester}/{department_id}/{academic_year}")
    public void calculateSGPA(@PathVariable String level, @PathVariable String semester, @PathVariable String department_id, @PathVariable String academic_year){
        arService.calculateSGPA(level, semester, department_id, academic_year);

        /*Usage
         */

    }


    /*---------------------------------------------------------------------------------------- Controller for Gpa table ----------------------------END-------------*/






    /*---------------------------------------------------------------------------------------- Controller for approval table ----------------------------START-------------*/


    @GetMapping("/getNotApprovedCoursesByLevelSemester/{level}/{semester}/{approval_level}/{academic_year}/{department_id}")            //Get * from marks Approval level table by selected level, semester, academic year and where approval level is not equal to provided level
    public List<MarksApprovalLevelDTO> getNotApprovedCoursesByLevelSemester(@PathVariable String level,@PathVariable String semester, @PathVariable String approval_level, @PathVariable String academic_year, @PathVariable String department_id){
        return arService.getNotApprovedCoursesByLevelSemester(level,semester, approval_level, academic_year, department_id);

        /*Usage
            CertifyMarksPage
            CreateResultsBoard
         */
    }


    @GetMapping("/getMarksApprovalLevelBySelectedCourseAndAcademicYear/{course_id}/{academic_year}")
    public List<MarksApprovalLevelDTO> getMarksApprovalLevelBySelectedCourseAndAcademicYear(@PathVariable String course_id, @PathVariable String academic_year){
        return arService.getMarksApprovalLevelBySelectedCourseAndAcademicYear(course_id,academic_year);

        /*Usage
            ViewMarksTableValidation
         */
    }


    @PutMapping("/updateApprovedLevelAfterResultBoard")
    public void updateApprovedLevelAfterResultBoard(@RequestBody ResultBoardDTO resultBoardDTO){
        arService.updateApprovedLevelAfterResultBoard(resultBoardDTO.getAcademic_year(), resultBoardDTO.getDepartment(), resultBoardDTO.getLevel(), resultBoardDTO.getSemester());

        /*Usage
            ResultBoardMarksSheetAssign
         */
    }


    /*---------------------------------------------------------------------------------------- Controller for approval table ----------------------------END-------------*/







    /*---------------------------------------------------------------------------------------- Controller for academic_year_details table ----------------------------START-------------*/
    @GetMapping("/getAcademicYearDetails")
    public List<AcademicYearDetailsDTO> getAcademicYearDetails(){
        return arService.getAcademicYearDetails();


        /*Usage
            UpdateABPage
            CreateResultBoard
         */
    }

    /*---------------------------------------------------------------------------------------- Controller for academic_year_details table ----------------------------END-------------*/








    /*---------------------------------------------------------------------------------------- Controller for User table ----------------------------START-------------*/

    @GetMapping("/findAllUserDetailsBySelectedRole/{role}")        //Get all user details by selected role
    public List<UserDTO> findAllUserDetailsBySelectedRole(@PathVariable String role){
        return arService.findAllUserDetailsBySelectedRole(role);
        /*Usage
            CreateResultBoard
        */

    }



    @GetMapping("/getAllCourseCoordinatorsBySelectedAcademicYearDepartmentLevelSemester/{academic_year}/{department_id}/{level}/{semester}")      //Get all course coordinator details by selected academic year department level and semester
    public List<UserDTO> getAllCourseCoordinatorsBySelectedAcademicYearDepartmentLevelSemester(@PathVariable String academic_year, @PathVariable String department_id, @PathVariable String level, @PathVariable String semester){
        return arService.getAllCourseCoordinatorsBySelectedAcademicYearDepartmentLevelSemester(academic_year, department_id, level, semester);
        /*Usage
            ResultBoardMarkSheetAssign
        */

    }




    /*---------------------------------------------------------------------------------------- Controller for User table ----------------------------END-------------*/







    /*---------------------------------------------------------------------------------------- Controller for result board table ----------------------------START-------------*/
    @GetMapping("/isResultBoardAvailable/{department}/{level}/{semester}/{academic_year}")                  //Get result board availability
    public boolean isResultBoardAvailable(@PathVariable String department,@PathVariable String level,@PathVariable String semester,@PathVariable String academic_year){
        return arService.isResultBoardAvailable(department, level, semester, academic_year);

        /*Usage
            CreateResultBoard
         */
    }

    @GetMapping("/getCreatedResultBoardList")                  //Get created result board list
    public List<ResultBoardDTO> getCreatedResultBoardList(){
        return arService.getCreatedResultBoardList();

        /*Usage
            CreateResultBoard
         */
    }

    @GetMapping("/getFinishedResultBoardList")                  //Get finished result board list
    public List<ResultBoardDTO> getFinishedResultBoardList(){
        return arService.getFinishedResultBoardList();

        /*Usage
            CertifyMarksPage
         */
    }

    @PostMapping("/saveResultBoard")                //Save result board
    public void saveResultBoard(@RequestBody ResultBoardDTO resultBoardDTO){
        arService.saveResultBoard(resultBoardDTO);

        /*Usage
            CreateResultBoard
         */
    }

    @GetMapping("/getResultBoardDetailsByID/{result_board_id}")                //Get result board details by result board id
    public ResultBoardDTO getResultBoardDetailsByID(@PathVariable int result_board_id){
        return arService.getResultBoardDetailsByID(result_board_id);

        /*Usage
            ResultsBoardMarksSheetAssign
        */
    }

    @DeleteMapping("/deleteResultBoardById/{result_board_id}")                //Delete result board
    public boolean deleteResultBoardById(@PathVariable int result_board_id){
        return arService.deleteResultBoardById(result_board_id);

        /*Usage
            CreateResultBoard
        */
    }

    @DeleteMapping("/deleteNotStartedResultBoard/{id}")                //Delete result board with relevant member records
    public int deleteNotStartedResultBoard(@PathVariable int id){
        return arService.deleteNotStartedResultBoard(id);

        /*Usage
            ResultsBoardMarksSheetAssign
        */
    }


    @GetMapping("/getCertifyPendingResultBoards/{approval_level}/{status}")                //Get result board details where AR can certify (Available for AR certification)
    public List<ResultBoardDTO> getCertifyPendingResultBoards(@PathVariable String approval_level, @PathVariable String status){
        return arService.getCertifyPendingResultBoards(approval_level, status);

        /*Usage
            CertifyMarksPage
        */
    }


    /*---------------------------------------------------------------------------------------- Controller for result board table ----------------------------END-------------*/







    /*---------------------------------------------------------------------------------------- Controller for result board member table ----------------------------START-------------*/

    @PostMapping("/saveResultBoardMember")
    public boolean saveResultBoardMember(@RequestBody ResultBoardMemberDTO resultBoardMemberDTO){         //Save result board member
        return arService.saveResultBoardMember(resultBoardMemberDTO);


        /*Usage
            ResultBoardMarksSheetAssign
        */
    }


    @GetMapping("/getAssignedMarksSheetsByResultBoardID/{result_board_id}")                //Get all assigned marks sheets by result board id
    public List<Object> getAssignedMarksSheetsByResultBoardID(@PathVariable int result_board_id){
        return arService.getAssignedMarksSheetsByResultBoardID(result_board_id);

        /*Usage
            ResultBoardMarksSheetAssign
        */
    }


    @GetMapping("/getAssignedMarksSheetsByExaminerIdAndResultBoardID/{result_board_id}/{course_coordinator_id}")                //Get all assigned marks sheets by result board id and course coordinator id
    public List<Object> getAssignedMarksSheetsByExaminerIdAndResultBoardID(@PathVariable int result_board_id, @PathVariable String course_coordinator_id){
        return arService.getAssignedMarksSheetsByExaminerIdAndResultBoardID(result_board_id, course_coordinator_id);

        /*Usage
            ResultBoardMarksSheetAssign
        */
    }


    @DeleteMapping("/deleteResultBoardMemberById/{result_board_member_id}")                //Delete result board member
    public boolean deleteResultBoardMemberById(@PathVariable int result_board_member_id){
        return arService.deleteResultBoardMemberById(result_board_member_id);

        /*Usage
            ResultBoardMarksSheetAssign
        */
    }

    @DeleteMapping("/deleteAssignedMarksSheetsByResultBoardID/{result_board_id}")
    public int deleteAssignedMarksSheetsByResultBoardID(@PathVariable int result_board_id){
        return arService.deleteAssignedMarksSheetsByResultBoardID(result_board_id);

        /*Usage
            ResultBoardMarksSheetAssign
        */

    }

    /*---------------------------------------------------------------------------------------- Controller for result board member table ----------------------------END-------------*/







    /*---------------------------------------------------------------------------------------- Controller for studentRegCourses table ----------------------------START-------------*/

    @GetMapping("/checkStudentRepeatStatus/{student_id}/{course_id}/{academic_year}")                //Check student repeat status
    public List<StudentRegCoursesDTO> checkStudentRepeatStatus(@PathVariable String student_id, @PathVariable String course_id, @PathVariable String academic_year){
        return arService.checkStudentRepeatStatus(student_id, course_id, academic_year);

        /*Usage
            UpdateABPage
        */
    }

    /*---------------------------------------------------------------------------------------- Controller for studentRegCourses table ----------------------------END-------------*/

}
