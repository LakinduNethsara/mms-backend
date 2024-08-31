package com.mms_backend.service;

import com.mms_backend.entity.EvaluationCriteria;
import com.mms_backend.entity.StudentRegCourses;
import com.mms_backend.repository.EvaluationCriteriaRepo;
import com.mms_backend.repository.MarksRepo;
import com.mms_backend.repository.StudentMarksRepo;
import com.mms_backend.repository.StudentRegCoursesRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EndSemesterCalculationService
{
    @Autowired
    private StudentRegCoursesRepo studentRegCoursesRepo;

    @Autowired
    private EvaluationCriteriaRepo evaluationCriteriaRepo;

    @Autowired
    private MarksRepo marksRepo;

    public void calculateESA(String course_id,String academic_year)
    {
        List<StudentRegCourses> list=studentRegCoursesRepo.getAllStudentsByCID(course_id,academic_year);
        List<EvaluationCriteria> evaluationCriteriaList=evaluationCriteriaRepo.getECbyCourseIDEnd(course_id);

        for(StudentRegCourses student:list)
        {
            //calculate ESA for student
            if(student.getIs_repeat()==0)
            {
                //calculate ESA for proper student
                for(EvaluationCriteria item:evaluationCriteriaList)
                {
//                    marksRepo.getMarksByStuIDCourseID_AY_ECID(course_id,academic_year,);
                }


            }
            else
            {
                //calculate ESA for Repeat student
            }
        }
    }
}
