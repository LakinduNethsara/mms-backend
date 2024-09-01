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

    public static String reduceYear(String years){

        String[] temp_Years_part = years.split("-");
        int year1 = Integer.parseInt(temp_Years_part[0]) -1;
        int year2 = Integer.parseInt(temp_Years_part[1]) -1;
        return year1+"-"+year2;
    }


    public void calculateRoundedMark(String course_id, String academic_year) {
        String pre_academic_year = reduceYear(academic_year);
        String pre_pre_academic_year =reduceYear(pre_academic_year);
        try {
//            //Get registered student for the course in the academic year (proper and repeat)
            List<StudentRegCourses> studentList = studentRegCoursesRepo.getAllStudentsByCID(course_id,academic_year);
//
//            //Map students
            for (StudentRegCourses student : studentList) {
//
                Grade grade= gradeRepo.getGradeDetailsBY_SIID_CID(student.getStudent_id(),course_id);
                System.out.println("Grade : "+grade.getStudent_id());
//
                if(student.getIs_repeat()==0){
                    //scenario for proper---------------------------------------------------------------------------------------------------------------
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

                }else{
                    //Scenario for repeat-----------------------------------------------------------------------------------------
                    if((grade.getGrade().equals("C-")) || (grade.getGrade().equals("D")) || (grade.getGrade().equals("D-")) || (grade.getGrade().equals("E"))){
////                        List<Calculations> markList = calculationsRepo.getStudentMarkPercentageList(course_id,student.getStudent_id(),academic_year);
////                        double sum=0.0 ;
////                        double roundedSum = 0.0 ;
////                        for (Calculations mark : markList) {
////                            sum= sum +Double.parseDouble(mark.getPercentage());
////                        }
////                        roundedSum = Math.round(sum*100)/100.00 ;
////                        System.out.println("Sum" + sum);
////                        System.out.println("Rounded Sum" + roundedSum);
////
////                        grade.setTotal_final_mark(String.valueOf(sum));
////                        grade.setTotal_rounded_mark(String.valueOf(roundedSum));
////
////                        gradeRepo.save(grade);
//
                        List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                        List<Calculations> pre_ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                        List<Calculations> pre_mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"Mid");
                        double sum = 0.0 ;
                        double roundedSum = 0.0;
//
                        if(!endCalculationList.isEmpty()){
                            for(Calculations endCalculation : endCalculationList){
                                sum = sum + Double.parseDouble(endCalculation.getPercentage());
                            }
//
                            if((!pre_ca_CalculationList.isEmpty()) && (!pre_mid_CalculationList.isEmpty())){
//

                                for(Calculations pre_mid_Calculation : pre_mid_CalculationList){
                                    sum = sum + Double.parseDouble(pre_mid_Calculation.getPercentage());
                                }

//
//

                                for(Calculations pre_ca_Calculation : pre_ca_CalculationList){
                                    sum = sum + Double.parseDouble(pre_ca_Calculation.getPercentage());
                                }

                                roundedSum = Math.round(sum*100)/100.00 ;

                                grade.setTotal_final_mark(String.valueOf(sum));
                                grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                gradeRepo.save(grade);




                            }else {

                                List<Calculations> pre_pre_ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_pre_academic_year,"CA");
                                List<Calculations> pre_pre_mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_pre_academic_year,"Mid");

                                if((!pre_pre_ca_CalculationList.isEmpty()) && (!pre_pre_mid_CalculationList.isEmpty())){

                                    for(Calculations pre_pre_ca_Calculation : pre_pre_ca_CalculationList){
                                        sum = sum + Double.parseDouble(pre_pre_ca_Calculation.getPercentage());
                                    }


                                    for(Calculations pre_pre_mid_Calculation : pre_pre_mid_CalculationList){
                                        sum = sum + Double.parseDouble(pre_pre_mid_Calculation.getPercentage());
                                    }

                                    roundedSum = Math.round(sum*100)/100.00 ;

                                    grade.setTotal_final_mark(String.valueOf(sum));
                                    grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                    gradeRepo.save(grade);

                                }else{

                                    List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");
                                    List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                                    if((!ca_CalculationList.isEmpty()) && (!mid_CalculationList.isEmpty())){
                                        for(Calculations ca_Calculation : ca_CalculationList){
                                            sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                        }


                                        for(Calculations mid_Calculation : mid_CalculationList){
                                            sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                        }

                                        roundedSum = Math.round(sum*100)/100.00 ;

                                        grade.setTotal_final_mark(String.valueOf(sum));
                                        grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                        gradeRepo.save(grade);

                                    }
                                }
                            }

                        }
                    }else if (grade.getGrade().equals("E*")){

                        List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                        List<Calculations> pre_ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                        List<Calculations> pre_mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"Mid");

                        


                    }
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
