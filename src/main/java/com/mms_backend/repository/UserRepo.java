package com.mms_backend.repository;

import com.mms_backend.entity.Student;
import com.mms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Integer> {
    @Query(nativeQuery = true,value = "select * from user where email=:email")
    User findByUserName(@Param("email") String email);

    @Query(nativeQuery = true,value = "select * from user where user_id=:student_id")
    User getStudentDetailsByStudentID(@Param("student_id") String student_id);

    @Query(nativeQuery = true,value = "select * from user where role='lecturer' and department_id=:department_id")
    List<User> findAllLecturersOfDepartment(@Param("department_id") String department_id);
}
