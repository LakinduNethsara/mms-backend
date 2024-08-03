package com.mms_backend.repository;

import com.mms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User,Integer> {
    @Query(nativeQuery = true,value = "select * from user where email=:email")
    User findByUserName(@Param("email") String email);
}
