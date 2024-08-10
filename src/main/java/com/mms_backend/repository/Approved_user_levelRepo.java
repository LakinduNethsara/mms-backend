package com.mms_backend.repository;


import com.mms_backend.entity.Marks_approved_log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Approved_user_levelRepo extends JpaRepository<Marks_approved_log,Integer> {

    @Query(nativeQuery = true, value = "select * from marks_approved_log where course_id=:course_id and approved_user_level=:approved_level and academic_year=:academic_year")
    Marks_approved_log getSignature(@Param("course_id")String course_id,@Param("approved_level") String approved_level,@Param("academic_year") String academic_year);

    @Query(nativeQuery = true, value = "select * from marks_approved_log where course_id=:course_id  and academic_year=:academic_year")
    List<Marks_approved_log> getSignatures(@Param("course_id")String course_id, @Param("academic_year") String academic_year);

    @Query(nativeQuery = true, value = "select * from marks_approved_log where  level=:level and semester=:semester and department_id=:department_id and approved_user_level=:approved_level and academic_year=:academic_year")
    Marks_approved_log getSignature(@Param("level") int level, @Param("semester") int semester, @Param("department_id")String department_id, @Param("approved_level") String approved_level, @Param("academic_year") String academic_year);

    @Modifying
    @Query(nativeQuery = true,value = "delete from marks_approved_log where course_id=:course_id and department_id=:department_id and academic_year=:academic_year")
    void removeSignature(@Param("course_id") String course_id, @Param("department_id")String department_id,  @Param("academic_year") String academic_year);
}
