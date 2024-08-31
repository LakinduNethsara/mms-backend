package com.mms_backend.repository;

import com.mms_backend.entity.NotificationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationsRepo extends JpaRepository<NotificationsEntity,Integer>
{
    @Query(nativeQuery = true,value = "SELECT * FROM scoredb.notifications where course_id=:course_id and status='send'")
    List<NotificationsEntity> getReturningReasons(@Param("course_id") String course_id);

    @Modifying
    @Query(nativeQuery = true,value = "update notifications set status='sent' where course_id=:course_id and status='send'")
    void updateNotificationState(@Param("course_id") String course_id);
}
