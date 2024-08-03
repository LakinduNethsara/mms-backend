package com.mms_backend.repository;

import com.mms_backend.entity.MarksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarksRepo extends JpaRepository<MarksEntity,Integer> {
    @Query(nativeQuery = true, value = "select * from marks  where course_id=:course_id ")
    List<MarksEntity> findStudentMarksByCourseID(@Param("course_id") String course_id);

    @Query(nativeQuery = true, value = "select * from marks where student_id=:student_id")
    List<MarksEntity> getScoreByStudent_ID(@Param("student_id") String student_id);

    @Query(nativeQuery = true, value = "select * from marks where level=:level and semester=:semester")
    List<MarksEntity> getScoreByLS(@Param("level") String level,@Param("semester") String semester);

    @Query(nativeQuery = true, value = "select * from marks where course_id=:course_id and student_id=:student_id")
    List<MarksEntity> getScoreByStuIDCourseID(@Param("course_id") String course_id,@Param("student_id") String student_id);

    @Modifying
    @Query(nativeQuery = true, value = "update marks set assignment_score=:value where student_id=:student_id and course_id=:course_id and assignment_name=:key")
    void updateEndMarks(@Param("value")String value,@Param("student_id")String student_id,@Param("course_id")String course_id,@Param("key")String key);

}
