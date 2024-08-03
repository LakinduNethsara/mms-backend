package com.mms_backend.repository;

import com.mms_backend.entity.LecturerCourseReg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LecturerCourseRegRepo extends JpaRepository<LecturerCourseReg,Integer> {
    @Query(nativeQuery = true, value = "select * from lecturer_course_reg where course_id=:course_id")
    List<LecturerCourseReg> getLecturersRegReposBy(@Param("course_id") String course_id);
}
