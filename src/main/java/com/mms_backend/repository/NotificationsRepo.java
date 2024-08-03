package com.mms_backend.repository;

import com.mms_backend.entity.NotificationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationsRepo extends JpaRepository<NotificationsEntity,Integer>
{
}
