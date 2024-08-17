package com.mms_backend.repository;

import com.mms_backend.entity.Marks_edit_log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarksEditLogRepo extends JpaRepository<Marks_edit_log,Integer> {
}
