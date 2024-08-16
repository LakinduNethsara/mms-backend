package com.mms_backend.repository.AR;

import com.mms_backend.entity.AR.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ARCourseRepo extends JpaRepository<Course,Integer> {


    @Query(nativeQuery = true,value = "select  DISTINCT course.id, course.course_id , course.course_name , course.hours ,course.type ,\n" +               // Get all courses of selected department by level and semester
            "course.department_id ,course.level ,course.semester from course inner join courses_related_departments on course.course_id = courses_related_departments.course_id where course.level=:level AND course.semester=:semester AND courses_related_departments.department_id=:department_id")
    List <Course> getViewMarksCourseList(String level, String semester,String department_id);


    //Get all the courses not added to the result board
    @Query(nativeQuery = true, value="select course.* from course inner join courses_related_departments on course.course_id=courses_related_departments.course_id  where course.level=:level AND course.semester=:semester AND courses_related_departments.department_id=:department_id AND course.course_id NOT IN (select course_id from result_board_member where result_board_member.result_board_id=:result_board_id)")
    List<Course> getCourseListRemainingToAddToResultBoard(int level, int semester, String department_id, int result_board_id);


    @Query(nativeQuery = true, value="SELECT distinct course.* FROM course INNER JOIN courses_related_departments ON courses_related_departments.course_id= course.course_id inner join studentregcourses on course.course_id= studentregcourses.course_id WHERE course.`level`=:level AND course.semester=:semester AND courses_related_departments.department_id=:department_id AND studentregcourses.academic_year=:academic_year")
    List<Course> getCourseDetailsForMarkSheet(int level, int semester, String department_id, String academic_year);     //Get list of all the grades by selected student id


}

