package com.mms_backend.repository;

import com.mms_backend.entity.CourseCoordinatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseCoordinatorRepo extends JpaRepository<CourseCoordinatorEntity,Integer>
{
    @Query(nativeQuery = true,value = "SELECT * FROM coursecoordinator where course_id =:course_id")
     CourseCoordinatorEntity getCCBycourse(@Param("course_id")String course_id);

    @Query(nativeQuery = true,value = "select cc.id, cc.user_id, cc.course_id, cc.academic_year from coursecoordinator cc left join evaluationcriteria ec on cc.course_id = ec.course_id where cc.user_id = (select user_id from user where email=:email) and ec.course_id is null")
    public List<CourseCoordinatorEntity> getAllCidToCourseCriteria(@Param("email")String email);

    @Query(nativeQuery = true,value = "SELECT user.email FROM coursecoordinator inner join user on user.user_id=coursecoordinator.user_id where course_id =:course_id")
    String getCourseCoordinatorEmail (@Param("course_id")String course_id);

}
