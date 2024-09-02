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

    @Query(value = "SELECT * FROM StudentRegCourses WHERE course_id =:course_id AND StudentRegCourses.is_repeat =:is_repeat AND StudentRegCourses.academic_year=:academicYear", nativeQuery = true)
    List<StudentRegCourses> getStudentsbyCourseCodeandRepeat(@Param("course_id") String course_id, @Param("is_repeat") int is_repeat,@Param("academicYear") String academicYear);

    @Query(nativeQuery = true,value = "select distinct studentregcourses.student_id from studentregcourses left join marks on studentregcourses.student_id=marks.student_id where studentregcourses.course_id=:course_id and marks.student_id is null ")
    List<String> getMarksNotEnteredStudents(@Param("course_id") String course_id);

    @Query(nativeQuery = true,value = "select distinct studentregcourses.* from studentregcourses left join marks on studentregcourses.student_id=marks.student_id where studentregcourses.course_id=:course_id and studentregcourses.academic_year=:academic_year")
    List<StudentRegCourses> getAllRegStudents(@Param("course_id") String course_id,@Param("academic_year") String academic_year);

    @Query(nativeQuery = true,value = "select * from studentregcourses where course_id=:course_id and academic_year=:academic_year")
    List<StudentRegCourses> getAllStudentsByCID(@Param("course_id") String course_id,@Param("academic_year") String academic_year);

    @Query(nativeQuery = true,value = "select * from studentregcourses where course_id=:course_id and academic_year=:academic_year and student_id=:student_id limit 1")
    StudentRegCourses getSelectedStudentRepeatDataByCID(@Param("course_id") String course_id,@Param("academic_year") String academic_year, @Param("student_id") String student_id);
}
