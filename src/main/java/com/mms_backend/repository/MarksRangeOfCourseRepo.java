package com.mms_backend.repository;

import com.mms_backend.entity.MarksRangeOfGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarksRangeOfCourseRepo extends JpaRepository<MarksRangeOfGrade,Integer> {

    @Query(nativeQuery = true,value = "select * from marks_range_of_grade where course_id=:course_id and academic_year=:academic_year")
    List<MarksRangeOfGrade> getMarksRange(@Param("course_id") String course_id, @Param("academic_year") String academic_year);

    @Query(nativeQuery = true,value = "select * from marks_range_of_grade where course_id=:course_id and academic_year=:academic_year and grade=:grade")
    MarksRangeOfGrade getMarkRangeofGrade(@Param("course_id") String course_id, @Param("academic_year") String academic_year,@Param("grade") String grade);
}
