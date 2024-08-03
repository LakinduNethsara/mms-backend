package com.mms_backend.repository;


import com.mms_backend.entity.StudentMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentMarksRepo extends JpaRepository<StudentMarks,Integer> {

    @Query(nativeQuery = true, value = "select * from grade g where g.level = :level and g.semester = :semester")
    List<StudentMarks> findStudentMarksByLevelSemester(@Param("level") String level, @Param("semester") String semester);

    @Query(nativeQuery = true, value = "SELECT s.id,s.student_id, s.course_id, s.level, s.semester, s.total_ca_mark, s.ca_eligibility, s.total_final_mark, s.total_rounded_mark, s.grade, s.gpv FROM scoredb.grade s inner join course on s.course_id=course.course_id inner join mark_approved_level on course.course_id=mark_approved_level.course_id inner join courses_related_departments on course.course_id=courses_related_departments.course_id where course.level=:level and course.semester=:semester and mark_approved_level.approval_level=:approved_level and courses_related_departments.department_id=:department_id")
    List<StudentMarks> findStudentMarksByLS(@Param("level") String level, @Param("semester") String semester,@Param("approved_level")String approved_level,@Param("department_id")String department_id);


    @Query(nativeQuery = true, value = "select * from grade  where grade.student_id=:id")
    List<StudentMarks> findCoursecodeOverallScoreByStId(@Param("id") String id);

    @Query(nativeQuery = true, value = "select * from grade where course_id=:course_id")
    List<StudentMarks> findMarksByCourse(@Param("course_id") String course_id);

    @Query(nativeQuery = true, value = "select * from grade where course_id=:course_id and student_id=:student_id")
    StudentMarks findMarksByCS(@Param("course_id") String course_id,@Param("student_id")String student_id);

}
