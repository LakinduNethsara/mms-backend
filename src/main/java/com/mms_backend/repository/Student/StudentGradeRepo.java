package com.mms_backend.repository.Student;

import com.mms_backend.entity.AR.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentGradeRepo extends JpaRepository<Grade,Integer> {

    @Query(nativeQuery = true, value = "select grade.* from grade where grade.student_id = :student_id and grade.grade = :grade")
    List<Grade> getGradeBySelectedStudentSelectedGrade(String student_id, String grade);     //Get list of all the grades by selected student id and selected grade

    @Query(nativeQuery = true, value="SELECT grade.* FROM grade WHERE grade.course_id IN (SELECT course.course_id FROM course INNER JOIN courses_related_departments ON courses_related_departments.course_id= course.course_id WHERE course.`level`=:level AND course.semester=:semester AND courses_related_departments.department_id=:department_id) AND grade.student_id=:student_id")
    List<Grade> getSelectedStudentGrade(int level, int semester, String department_id, String student_id);     //Get list of all the grades by selected student id


    @Query(nativeQuery = true, value="SELECT grade.* FROM grade WHERE grade.course_id IN (SELECT course.course_id FROM course INNER JOIN courses_related_departments ON courses_related_departments.course_id= course.course_id inner join studentregcourses on course.course_id= studentregcourses.course_id WHERE course.`level`=:level AND course.semester=:semester AND courses_related_departments.department_id=:department_id AND studentregcourses.academic_year=:academic_year) AND grade.student_id IN (SELECT studentregcourses.student_id FROM course INNER JOIN courses_related_departments ON courses_related_departments.course_id= course.course_id inner join studentregcourses on course.course_id= studentregcourses.course_id WHERE course.`level`=:level AND course.semester=:semester AND courses_related_departments.department_id=:department_id AND studentregcourses.academic_year=:academic_year) order by grade.course_id asc, grade.student_id asc ")
    List<Grade> getGradesForPublishedMarksSheet(int level, int semester, String department_id, String academic_year);     //Get list of all the grades by selected student id

}
