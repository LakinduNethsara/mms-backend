package com.mms_backend.repository;


import com.mms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Integer> {
    @Query(nativeQuery = true,value = "select * from user where role != 'student' and email=:email")
    User findByUserName(@Param("email") String email);

    @Query(nativeQuery = true,value = "select * from user where user_id=:student_id")
    User getStudentDetailsByStudentID(@Param("student_id") String student_id);

    @Query(nativeQuery = true,value = "select * from user where role='lecturer' and department_id=:department_id and is_deleted=false")
    List<User> findAllLecturersOfDepartment(@Param("department_id") String department_id);

    @Query(nativeQuery = true,value = "select * from user where role NOT IN ('student') and is_deleted=false order by id desc")
    List<User> findStaff();

    @Query(nativeQuery = true,value = "select * from user where role='lecturer' and is_deleted=false order by id desc")
    List<User> findAllLecturers();

    @Query(nativeQuery = true,value = "select email from user where department_id=:department_id and role=:role")
    String getEmail(@Param("department_id") String department_id,@Param("role") String role);

    @Query(nativeQuery = true,value = "select email from user where  role=:role")
    String getEmailByRole(@Param("role") String role);

}
