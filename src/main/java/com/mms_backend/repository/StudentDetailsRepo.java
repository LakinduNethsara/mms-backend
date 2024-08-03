package com.mms_backend.repository;

import com.mms_backend.entity.StudentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentDetailsRepo extends JpaRepository<StudentDetailsEntity,Integer> {

}
