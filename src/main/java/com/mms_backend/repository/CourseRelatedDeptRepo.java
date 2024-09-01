package com.mms_backend.repository;

import com.mms_backend.entity.CourseRelatedDeptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRelatedDeptRepo extends JpaRepository<CourseRelatedDeptEntity,Integer> {

    @Query(nativeQuery = true,value = "SELECT * FROM courses_related_departments where course_id =:course_id")
    List<CourseRelatedDeptEntity> getDeptByCourse(@Param("course_id")String course_id);


}
