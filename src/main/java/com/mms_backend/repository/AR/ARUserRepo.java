package com.mms_backend.repository.AR;

import com.mms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ARUserRepo extends JpaRepository<User,Integer> {
    @Query(nativeQuery = true, value = "select * from user where role=:role")       //Get all user details by selected role
    List<User> findAllUserDetailsBySelectedRole(String role);

    @Query(nativeQuery = true,value = "select distinct user.* from user inner join coursecoordinator on coursecoordinator.user_id=user.user_id inner join courses_related_departments on courses_related_departments.course_id= coursecoordinator.course_id inner join course on course.course_id=coursecoordinator.course_id where coursecoordinator.academic_year=:academic_year AND courses_related_departments.department_id=:department_id AND course.level=:level AND course.semester=:semester AND user.role!='student'")       //Get all course coordinator details by selected academic year department level and semester
    List<User> getAllCourseCoordinatorsBySelectedAcademicYearDepartmentLevelSemester(String academic_year, String department_id, String level, String semester);

}
