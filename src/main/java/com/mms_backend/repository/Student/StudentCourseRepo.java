package com.mms_backend.repository.Student;

import com.mms_backend.entity.AR.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentCourseRepo extends JpaRepository<Course,Integer> {

    @Query(nativeQuery = true, value = "select course.* from course order by course.department_id asc, course.level asc, course.semester asc")
    List<Course> getAllCourses();     //Get list of all the courses

    @Query(nativeQuery = true, value="select course.* from course inner join courses_related_departments on course.course_id = courses_related_departments.course_id where courses_related_departments.department_id = :department_id order by course.level asc, course.semester asc")
    List<Course> getCourseListByDepartment(String department_id);     //Get list of all the courses by selected department id

    @Query (nativeQuery = true, value = " select course.*, grade.ca_eligibility from course inner join studentregcourses on course.course_id = studentregcourses.course_id LEFT JOIN grade on grade.student_id=studentregcourses.student_id and grade.course_id= studentregcourses.course_id where studentregcourses.student_id = :student_id and studentregcourses.academic_year = :academic_year and course.semester=:semester")
    List<Object> getStudentCourseListBySelectedYear(String student_id, String academic_year, int semester);     //Get list of all the courses by selected student id and selected academic year and semester



    @Query(nativeQuery = true, value = "select course.* from course inner join studentregcourses on course.course_id = studentregcourses.course_id where studentregcourses.student_id =:student_id order by course.level desc, course.semester desc limit 1")
    Course getStudentLevelAndSemester (String student_id);        //Get student current level and semester


    @Query(nativeQuery = true, value="SELECT distinct course.* FROM course INNER JOIN courses_related_departments ON courses_related_departments.course_id= course.course_id inner join studentregcourses on course.course_id= studentregcourses.course_id WHERE course.`level`=:level AND course.semester=:semester AND courses_related_departments.department_id=:department_id AND studentregcourses.academic_year=:academic_year")
    List<Course> getCourseDetailsForPublishedMarkSheet(int level, int semester, String department_id, String academic_year);     //Get all course details for mark sheet view by selected department, level, semester and academic year



}
