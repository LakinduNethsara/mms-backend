package com.mms_backend.repository;

import com.mms_backend.entity.Calculations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalculationsRepo extends JpaRepository<Calculations,Integer>
{
    @Query(nativeQuery = true, value = "select markcalculations.* from markcalculations inner join studentregcourses on studentregcourses.student_id=markcalculations.student_id where studentregcourses.course_id=:course_id and studentregcourses.academic_year=:academic_year")
    public List<Calculations> getStudentsCalculationresults(@Param("course_id")String course_id,@Param("academic_year")String academic_year);

    @Query(nativeQuery = true, value = "select * from markcalculations where course_id=:course_id and student_id=:student_id")
    public List<Calculations> getCalculationresults(@Param("course_id")String course_id,@Param("student_id")String student_id);

    @Query(nativeQuery = true, value = "select markcalculations.mark, evaluationcriteria.percentage from markcalculations inner join evaluationcriteria on evaluationcriteria.evaluationcriteria_id = markcalculations.evaluation_criteria_id inner join assessment_type_list on assessment_type_list.assessment_type_name = evaluationcriteria.assessment_type where markcalculations.student_id=:student_id and markcalculations.course_id=:course_id and markcalculations.academic_year=:academic_year and assessment_type_list.ca_mid_end='CA';")
    public List<Object[]> getOLDCAByStuIDCourseID_AY(@Param("student_id") String student_id,@Param("course_id") String course_id,@Param("academic_year") String academic_year);

    @Query (nativeQuery = true, value = "select markcalculations.* from markcalculations where evaluation_criteria_id in (select evaluationcriteria_id from evaluationcriteria where course_id=:course_id) and student_id=:student_id and academic_year=:academic_year")
    public List<Calculations> getStudentMarkPercentageList(String course_id, String student_id, String academic_year);
}
