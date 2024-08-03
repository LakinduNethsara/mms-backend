package com.mms_backend.repository;

import com.mms_backend.entity.GPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GPARepo extends JpaRepository<GPA,Integer>
{
    @Query(nativeQuery = true, value = "select * from gpa  where level=:level and semester=:semester")
   List<GPA>  findGPAByLevelSemester(@Param("level") String level,@Param("semester")String semester);

    @Query(nativeQuery = true, value = "select * from gpa  where student_id=:student_id")
    GPA  findGPAByStId(@Param("student_id") String student_id);
}
