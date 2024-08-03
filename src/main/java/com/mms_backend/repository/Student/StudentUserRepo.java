package com.mms_backend.repository.Student;

import com.mms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentUserRepo extends JpaRepository<User,Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM user WHERE email = :email and user.role='student' LIMIT 1")
    User getStudentDetailsByEmail(String email);              // get student details by email
}
