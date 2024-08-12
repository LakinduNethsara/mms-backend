package com.mms_backend.repository;

import com.mms_backend.entity.LecturersRegEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LecturersRegRepo extends JpaRepository<LecturersRegEntity,Integer> {


}
