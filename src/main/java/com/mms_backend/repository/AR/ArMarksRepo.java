package com.mms_backend.repository.AR;

import com.mms_backend.entity.MarksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArMarksRepo extends JpaRepository<MarksEntity,Integer> {



    //Get all  students records to list down from marks table having AB s for valid exams
    @Query(nativeQuery = true, value="select distinct course.level, course.semester, course.course_id, course.course_name, marks.student_id, marks.assignment_score, evaluationcriteria.assessment_type, mark_approved_level.approval_level, marks.academic_year, marks.assignment_name, assessment_type_list.ca_mid_end from (((((marks inner join mark_approved_level on marks.course_id=mark_approved_level.course_id AND marks.academic_year =mark_approved_level.academic_year ) inner join course on course.course_id=marks.course_id) inner join evaluationcriteria on evaluationcriteria.evaluationcriteria_id=marks.evaluation_criteria_id) inner join evaluationcriteria_name on evaluationcriteria_name.evaluationcriteria_id= marks.evaluation_criteria_id) inner join assessment_type_list on assessment_type_list.assessment_type_name = evaluationcriteria.assessment_type) WHERE ((marks.assignment_score='AB') AND (assessment_type_list.ca_mid_end='Mid' or assessment_type_list.ca_mid_end='End') AND mark_approved_level.approval_level=:approved_level) order by course.level, course.semester, course.course_id, marks.student_id")
    List<Object[]> getABDetails(String approved_level);


    //get E* details by selected course id
    @Query(nativeQuery = true, value="select distinct course.level, course.semester, course.course_id, course.course_name, marks.student_id, marks.assignment_score, evaluationcriteria.assessment_type, mark_approved_level.approval_level, marks.academic_year from (((((marks inner join mark_approved_level on marks.course_id=mark_approved_level.course_id AND marks.academic_year =mark_approved_level.academic_year ) inner join course on course.course_id=marks.course_id) inner join evaluationcriteria on evaluationcriteria.evaluationcriteria_id=marks.evaluation_criteria_id) inner join evaluationcriteria_name on evaluationcriteria_name.evaluationcriteria_id= marks.evaluation_criteria_id) inner join assessment_type_list on assessment_type_list.assessment_type_name = evaluationcriteria.assessment_type) WHERE ((marks.assignment_score='AB' AND marks.course_id=:course_id) AND (assessment_type_list.ca_mid_end = 'Mid' or assessment_type_list.ca_mid_end='End')) order by course.level, course.semester, course.course_id, marks.student_id")
    List<Object[]> getABDetailsByCourseId(String course_id);

    //Update E* details of selected student--------
    @Modifying
    @Query(nativeQuery = true, value = "update marks inner join evaluationcriteria on marks.evaluation_criteria_id=evaluationcriteria.evaluationcriteria_id " +
            "set marks.assignment_score=:assignment_score where marks.student_id=:student_id AND marks.course_id=:course_id AND marks.academic_year=:academic_year AND  evaluationcriteria.assessment_type=:exam_type")
    int updateStudentScore(String assignment_score, String student_id, String course_id, String academic_year , String exam_type);


    //Get all from marks table by providing student id , course id, academic year, and exam type
    @Query(nativeQuery = true,value = "select marks.* from marks inner join evaluationcriteria on marks.evaluation_criteria_id= evaluationcriteria.evaluationcriteria_id inner join assessment_type_list on assessment_type_list.assessment_type_name= evaluationcriteria.assessment_type where marks.student_id=:student_id AND marks.course_id=:course_id AND marks.academic_year=:academic_year AND assessment_type_list.ca_mid_end=:exam_type")
    List <MarksEntity> getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(String student_id, String course_id, String academic_year, String exam_type);


    //Check whether there are any absence students in the selected department, selected level, selected semester, selected academic year for end or mid
    @Query(nativeQuery = true, value="select marks.* from (((marks inner join courses_related_departments on marks.course_id=courses_related_departments.course_id) inner join evaluationcriteria on marks.evaluation_criteria_id = evaluationcriteria.evaluationcriteria_id) inner join course on courses_related_departments.course_id = course.course_id)  where marks.academic_year=:academic_year AND course.semester=:semester AND course.level=:level AND courses_related_departments.department_id=:department_id AND marks.assignment_score='AB' AND (evaluationcriteria.assessment_type='End theory exam' OR evaluationcriteria.assessment_type='End practical exam' OR evaluationcriteria.assessment_type='Mid theory exam' OR evaluationcriteria.assessment_type='Mid practical exam')")
    List<MarksEntity> isABStudentAvailable(String academic_year, String semester, String level, String department_id );
}
