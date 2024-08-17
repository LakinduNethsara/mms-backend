package com.mms_backend.repository;

import com.mms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentDetailsRepo extends JpaRepository<User,Integer> {

    @Query(nativeQuery = true,value = "select * from user where role='student'")
    List<User> findAllStudents();
}
