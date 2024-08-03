package com.mms_backend.repository;

import com.mms_backend.entity.CourseCoordinatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseCoordinatorRepo extends JpaRepository<CourseCoordinatorEntity,Integer>
{
    @Query(nativeQuery = true,value = "SELECT * FROM coursecoordinator where course_id =:course_id")
    public CourseCoordinatorEntity getCCBycourse(@Param("course_id")String course_id);

    @Query(nativeQuery = true,value = "select cc.id, cc.user_id, cc.course_id, cc.academic_year from coursecoordinator cc left join evaluationcriteria ec on cc.course_id = ec.course_id where cc.user_id = (select user_id from user where user_name=:user_name) and ec.course_id is null")
    public List<CourseCoordinatorEntity> getAllCidToCourseCriteria(@Param("user_name")String user_name);

}
