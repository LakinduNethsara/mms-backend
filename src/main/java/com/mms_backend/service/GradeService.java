package com.mms_backend.service;

import com.mms_backend.entity.AR.Grade;
import com.mms_backend.entity.Calculations;
import com.mms_backend.entity.StudentRegCourses;
import com.mms_backend.repository.CalculationsRepo;
import com.mms_backend.repository.GradeRepo;
import com.mms_backend.repository.StudentRegCoursesRepo;
import com.mms_backend.repository.StudentRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GradeService {
    @Autowired
    private GradeRepo gradeRepo;
    @Autowired
    private StudentRegCoursesRepo studentRegCoursesRepo;
    @Autowired
    private CalculationsRepo calculationsRepo;


    public void calculateRoundedMark(String course_id, String academic_year) {
        try {
            //Get registered student for the course in the academic year (proper and repeat)
            List<StudentRegCourses> studentList = studentRegCoursesRepo.getAllStudentsByCID(course_id,academic_year);

            //Map students
            for (StudentRegCourses student : studentList) {

                Grade grade= gradeRepo.getGradeDetailsBY_SIID_CID(student.getStudent_id(),course_id);
                System.out.println("Grade : "+grade.getStudent_id());

                List<Calculations> markList = calculationsRepo.getStudentMarkPercentageList(course_id,student.getStudent_id(),academic_year);
                double sum=0.0 ;
                double roundedSum = 0.0 ;
                for (Calculations mark : markList) {
                    sum= sum +Double.parseDouble(mark.getPercentage());
                }
                roundedSum = Math.round(sum*100)/100.00 ;
                System.out.println("Sum" + sum);
                System.out.println("Rounded Sum" + roundedSum);

                grade.setTotal_final_mark(String.valueOf(sum));
                grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                gradeRepo.save(grade);



            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }



    }
}
