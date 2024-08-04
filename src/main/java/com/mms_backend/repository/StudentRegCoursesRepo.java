package com.mms_backend.repository;

import com.mms_backend.entity.StudentRegCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRegCoursesRepo extends JpaRepository<StudentRegCourses,String>
{
    @Query(nativeQuery = true,value = "select * from StudentRegCourses  where course_id=:course_id")
    List<StudentRegCourses> getStudentsbyCourseCode(@Param("course_id") String course_id);

    @Query(nativeQuery = true,value = "select distinct studentregcourses.student_id from studentregcourses left join marks on studentregcourses.student_id=marks.student_id where studentregcourses.course_id=:course_id and marks.student_id is null ")
    List<String> getMarksNotEnteredStudents(@Param("course_id") String course_id);

    @Query(nativeQuery = true,value = "select distinct studentregcourses.student_id from studentregcourses left join marks on studentregcourses.student_id=marks.student_id where studentregcourses.course_id=:course_id")
    List<String> getAllRegStudents(@Param("course_id") String course_id);
}
