package com.mms_backend.service;

import com.mms_backend.repository.StudentMarksRepo;
import com.mms_backend.repository.StudentRegCoursesRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EndSemesterCalculationService
{
    @Autowired
    private StudentRegCoursesRepo studentRegCoursesRepo;

    public void calculateESA(String course_id)
    {
        studentRegCoursesRepo.
    }
}
