package com.mms_backend.repository.AR;

import com.mms_backend.entity.AR.MarksApprovalLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ARMarksApprovalLevelRepo extends JpaRepository<MarksApprovalLevel,Integer> {




    //Get * from marks Approval level table by selected level, semester, academic year, department and where approval level is not equal to provided level
    @Query(nativeQuery = true, value="select mark_approved_level.* from mark_approved_level inner join course on mark_approved_level.course_id=course.course_id where" +
            " course.level=:level AND course.semester=:semester AND mark_approved_level.approval_level!=:approval_level AND mark_approved_level.academic_year=:academic_year AND mark_approved_level.department_id=:department_id")
    List<MarksApprovalLevel> getNotApprovedCoursesByLevelSemester(String level,String semester, String approval_level, String academic_year, String department_id);

    //This method is to find * details of mark approval level table with passing course id and academic year
    @Query(value = "select * from mark_approved_level where course_id=:course_id and academic_year=:academic_year ",nativeQuery = true)
    List<MarksApprovalLevel> getMarksApprovalLevelBySelectedCourseAndAcademicYear(String course_id, String academic_year );


    //Update approved level after result board finished
    @Modifying
    @Query (nativeQuery = true, value ="UPDATE  mark_approved_level INNER JOIN course ON course.course_id= mark_approved_level.course_id SET mark_approved_level.approval_level='RB' WHERE mark_approved_level.academic_year =:academic_year AND mark_approved_level.department_id =:department_id AND course.level =:level AND course.semester =:semester")
    void updateApprovedLevelAfterResultBoard(String academic_year, String department_id, int level, int semester);
}
