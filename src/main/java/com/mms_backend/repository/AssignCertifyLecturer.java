package com.mms_backend.repository;

import com.mms_backend.entity.Assigncertifylecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignCertifyLecturer extends JpaRepository<Assigncertifylecturer,Integer> {

    @Query(nativeQuery = true, value ="delete from assigncertifylecturer wher course_id=:course_id")
    void returningResultSheet(@Param("course_id") String course_id);
}
