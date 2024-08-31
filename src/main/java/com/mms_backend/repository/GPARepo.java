package com.mms_backend.repository;

import com.mms_backend.entity.GPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GPARepo extends JpaRepository<GPA,Integer>
{
    @Query(nativeQuery = true, value = "select * from gpa \n" +
            " where level=1 and semester=1 and student_id IN (select student_id from studentregcourses \n" +
            " inner join courses_related_departments on courses_related_departments.course_id=studentregcourses.course_id\n" +
            " inner join mark_approved_level on mark_approved_level.course_id=courses_related_departments.course_id\n" +
            " inner join course on course.course_id=courses_related_departments.course_id\n" +
            " where \n" +
            " course.level=:level and course.semester=:semester and courses_related_departments.department_id=:department_id and mark_approved_level.approval_level=:approval_level and studentregcourses.is_repeat=:repeat)")
   List<GPA>  findGPAByLevelSemester(@Param("level") String level, @Param("semester")String semester, @Param("department_id")String department_id, @Param("approval_level")String approval_level, int repeat);

    @Query(nativeQuery = true, value = "select * from gpa  where student_id=:student_id")
    GPA  findGPAByStId(@Param("student_id") String student_id);
}
