package com.mms_backend.repository;

import com.mms_backend.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepo extends JpaRepository<CourseEntity,Integer> {

    @Query(nativeQuery = true, value = "SELECT c.id, c.course_id,c.course_name,c.hours,c.type,c.credit,c.department_id,c.level,c.semester FROM course c inner join mark_approved_level on c.course_id=mark_approved_level.course_id where  c.department_id=:department and mark_approved_level.approval_level=:approved_level and c.level=:level and c.semester=:sem and academic_year=:academic_year" )
    List<CourseEntity> findApprovedCourses(@Param("level")int level, @Param("sem") int semester, @Param("department") String department, @Param("approved_level") String approved_level, @Param("academic_year") String year);

    @Query(nativeQuery = true,value = "SELECT c.id, c.course_id,c.course_name,c.hours,c.type,c.credit,c.department_id,c.level,c.semester FROM course c INNER JOIN coursecoordinator cc ON c.course_id = cc.course_id inner join mark_approved_level on c.course_id = mark_approved_level.course_id WHERE cc.user_id = (SELECT user_id FROM user WHERE user_name =:user_name) and mark_approved_level.approval_level = 'finalized'")
    List<CourseEntity> findLecturerApprovedCourses(@Param("user_name") String user_name);

    @Query(nativeQuery = true,value = "select c.id, c.course_id,c.course_name,c.hours,c.type,c.credit,c.department_id,c.level,c.semester from course c inner join coursecoordinator cc on  c.course_id = cc.course_id where cc.user_id =(select user_id from user where user_name =:user_name)")
    List<CourseEntity> findCCRegCourses(@Param("user_name") String user_name);

    @Query(nativeQuery = true,value = "select c.id, c.course_id,c.course_name,c.hours,c.type,c.credit,c.department_id,c.level,c.semester from course c inner join courses_related_departments crd on c.course_id=crd.course_id  where c.level=:level and c.semester=:sem and crd.department_id=:department")
    List<CourseEntity> findAllcourseOfDeptLS(@Param("level")int level, @Param("sem") int semester,@Param("department") String department);

    @Query(nativeQuery = true,value="select c.id, c.course_id,c.course_name,c.hours,c.type,c.credit,c.department_id,c.level,c.semester from course c inner join assigncertifylecturer a on c.course_id=a.course_id inner join mark_approved_level on c.course_id = mark_approved_level.course_id where mark_approved_level.approval_level = 'course_coordinator' and a.lecturer_id=:lecturer_id")
    List<CourseEntity> findLecturerCertifiedAssignCourse(@Param("lecturer_id") String lecturer_id);
}
