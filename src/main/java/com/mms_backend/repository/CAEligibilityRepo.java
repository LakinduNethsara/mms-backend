package com.mms_backend.repository;

import com.mms_backend.entity.CAEligibilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CAEligibilityRepo extends JpaRepository <CAEligibilityEntity,Integer>{
    @Query(nativeQuery = true, value = "select * from grade where course_id=:course_id")
    List<CAEligibilityEntity> findAllCbyCid(@Param("course_id")String course_id);
}
