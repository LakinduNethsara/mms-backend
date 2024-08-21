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
}
