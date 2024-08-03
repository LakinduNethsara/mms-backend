package com.mms_backend.repository;

import com.mms_backend.entity.MedicalEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MedicalRepo extends JpaRepository<MedicalEntity,Integer> {
}
