package com.mms_backend.repository;

import com.mms_backend.entity.AR.MarksApprovalLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApprovalLevelRepo extends JpaRepository<MarksApprovalLevel,Integer>
{

    @Modifying
    @Query(nativeQuery = true,value ="UPDATE mark_approved_level SET approval_level =:approval_level WHERE course_id=:course_id and academic_year=:academic_year")
    void updateApprovedLevel(@Param("course_id") String course_id, @Param("academic_year")String academic_year, @Param("approval_level") String approval_level);

    @Modifying
    @Query(nativeQuery = true,value ="UPDATE mark_approved_level INNER JOIN course  ON course.course_id=mark_approved_level.course_id inner join courses_related_departments on course.course_id=courses_related_departments.course_id SET mark_approved_level.approval_level =:approval_level WHERE course.level=:level AND course.semester=:sem  AND courses_related_departments.department_id=:department_id AND mark_approved_level.academic_year =:academic_year")
    void updateApprovedLevelByDean(@Param("level") int level, @Param("sem") int sem, @Param("academic_year")String academic_year, @Param("department_id")String department_id, @Param("approval_level") String approval_level);

    @Query(nativeQuery = true,value = "select * from mark_approved_level where course_id=:course_id")
    MarksApprovalLevel getApprovalLevel(@Param("course_id")String course_id);


}

