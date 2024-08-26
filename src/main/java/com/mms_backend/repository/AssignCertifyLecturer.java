package com.mms_backend.repository;

import com.mms_backend.entity.Assigncertifylecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssignCertifyLecturer extends JpaRepository<Assigncertifylecturer,Integer> {

    @Modifying
    @Query(nativeQuery = true, value ="delete from assigncertifylecturer where course_id=:course_id and department_id=:department_id")
    void returningResultSheet(@Param("course_id") String course_id, @Param("department_id") String department_id);

    @Query(nativeQuery = true,value = "select user.email from assigncertifylecturer inner join user on user.email=assigncertifylecturer.lecturer_id  where course_id=:course_id and assigncertifylecturer.department_id=:department_id")
    String getEmail(@Param("course_id") String course_id,@Param("department_id") String department_id);
}
