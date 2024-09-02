package com.mms_backend.repository;

import com.mms_backend.entity.GPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GPARepo extends JpaRepository<GPA,Integer>
{
    @Query(nativeQuery = true, value = "SELECT * FROM gpa WHERE student_id IN (SELECT studentregcourses.student_id FROM studentregcourses  INNER JOIN courses_related_departments ON courses_related_departments.course_id = studentregcourses.course_id  INNER JOIN mark_approved_level ON mark_approved_level.department_id = courses_related_departments.department_id  INNER JOIN course ON course.course_id = courses_related_departments.course_id  WHERE course.level =:level AND course.semester =:semester AND courses_related_departments.department_id =:department_id AND   mark_approved_level.approval_level=:approval_level AND studentregcourses.is_repeat=:ISrepeat)")
   List<GPA>  findGPAByLevelSemester(@Param("level") String level, @Param("semester")String semester, @Param("department_id")String department_id, @Param("approval_level")String approval_level,@Param("ISrepeat") int repeat);

    @Query(nativeQuery = true, value = "select * from gpa  where student_id=:student_id")
    GPA  findGPAByStId(@Param("student_id") String student_id);
}
