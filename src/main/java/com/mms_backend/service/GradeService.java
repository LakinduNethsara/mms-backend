package com.mms_backend.service;

import com.mms_backend.entity.*;
import com.mms_backend.entity.AR.Grade;
import com.mms_backend.entity.AR.MarksApprovalLevel;
import com.mms_backend.repository.*;
import com.mms_backend.repository.AR.ArMarksRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
public class GradeService {
    @Autowired
    private GradeRepo gradeRepo;
    @Autowired
    private StudentRegCoursesRepo studentRegCoursesRepo;
    @Autowired
    private CalculationsRepo calculationsRepo;
    @Autowired
    private MarksRepo marksRepo;
    @Autowired
    private ArMarksRepo arMarksRepo;
    @Autowired
    private CourseRelatedDeptRepo courseRelatedDeptRepo;
    @Autowired
    private MarksRangeOfCourseRepo marksRangeOfCourseRepo;

    public static String reduceYear(String years){

        String[] temp_Years_part = years.split("-");
        int year1 = Integer.parseInt(temp_Years_part[0]) -1;
        int year2 = Integer.parseInt(temp_Years_part[1]) -1;
        return year1+"-"+year2;
    }

    @Autowired
    private ApprovalLevelRepo approvalLevelRepo;


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
                    System.out.println("This is proper-------------------------------");
                    List<Calculations> markList = calculationsRepo.getStudentMarkPercentageList(course_id,student.getStudent_id(),academic_year);
                    System.out.println("Mark list is");
                    System.out.println(markList);
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

                    List<MarksRangeOfGrade> marginList = null;
                    try{
                        marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                        if(!marginList.isEmpty()){
                            if(grade.getOverall_eligibility().equals("Eligible")){
                                grade.setGrade(marginList.get(0).getGrade());

                            }else if (grade.getOverall_eligibility().equals("Not eligible")){
                                grade.setGrade("F");
                            }else if(grade.getOverall_eligibility().equals("WH")){
                                grade.setGrade("WH");
                            }
                        }

                    }catch (Exception e){
                        System.out.println("Grade is not saved "+e.getMessage());

                    }


                    System.out.println("Student "+student.getStudent_id()+ " Grade - "+grade.getGrade());

                    gradeRepo.save(grade);


                }else{
                    //Scenario for repeat-----------------------------------------------------------------------------------------
                    System.out.println("This is repeat-------------------------------");
                    if((grade.getGrade().equals("C-")) || (grade.getGrade().equals("D")) || (grade.getGrade().equals("D+")) || (grade.getGrade().equals("E"))){
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

                                MarksRangeOfGrade marksRangeOfGrade = null;
                                try{
                                    marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");

                                    if(grade.getCa_eligibility().equals("Eligible")){

                                        if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){

                                            grade.setGrade("C");
                                        } else{
                                            List<MarksRangeOfGrade> marginList = null;
                                            try{
                                                marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                                if(!marginList.isEmpty()){
                                                    grade.setGrade(marginList.get(0).getGrade());

                                                }

                                            }catch (Exception e){
                                                log.error("e: ", e);
                                            }
                                        }

                                    } else if (grade.getCa_eligibility().equals("Not eligible")) {
                                        grade.setGrade("F");

                                    } else if (grade.getCa_eligibility().equals("WH")) {
                                        grade.setGrade("WH");
                                    }


                                }catch (Exception ex){
                                    log.error("e: ", ex);
                                }


//                                gradeRepo.save(grade);




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


                                    MarksRangeOfGrade marksRangeOfGrade = null;
                                    try{
                                        marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");

                                        if(grade.getCa_eligibility().equals("Eligible")){

                                            if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){

                                                grade.setGrade("C");
                                            } else{
                                                List<MarksRangeOfGrade> marginList = null;
                                                try{
                                                    marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                                    if(!marginList.isEmpty()){
                                                        grade.setGrade(marginList.get(0).getGrade());

                                                    }

                                                }catch (Exception e){
                                                    log.error("e: ", e);
                                                }
                                            }

                                        } else if (grade.getCa_eligibility().equals("Not eligible")) {
                                            grade.setGrade("F");

                                        } else if (grade.getCa_eligibility().equals("WH")) {
                                            grade.setGrade("WH");
                                        }


                                    }catch (Exception ex){
                                        log.error("e: ", ex);
                                    }

//                                    gradeRepo.save(grade);

                                }else{

                                    List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");
                                    List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                                    if((!ca_CalculationList.isEmpty()) && (!mid_CalculationList.isEmpty())){

                                        if(grade.getCa_eligibility().equals("Eligible")){

                                            for(Calculations ca_Calculation : ca_CalculationList){
                                                sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                            }


                                            for(Calculations mid_Calculation : mid_CalculationList){
                                                sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                            }

                                            roundedSum = Math.round(sum*100)/100.00 ;

                                            grade.setTotal_final_mark(String.valueOf(sum));
                                            grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                            MarksRangeOfGrade marksRangeOfGrade = null;
                                            try{
                                                marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");

                                                if(grade.getCa_eligibility().equals("Eligible")){

                                                    if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){

                                                        grade.setGrade("C");
                                                    } else{
                                                        List<MarksRangeOfGrade> marginList = null;
                                                        try{
                                                            marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                                            if(!marginList.isEmpty()){
                                                                grade.setGrade(marginList.get(0).getGrade());

                                                            }

                                                        }catch (Exception e){
                                                            log.error("e: ", e);
                                                        }
                                                    }

                                                } else if (grade.getCa_eligibility().equals("Not eligible")) {
                                                    grade.setGrade("F");

                                                } else if (grade.getCa_eligibility().equals("WH")) {
                                                    grade.setGrade("WH");
                                                }


                                            }catch (Exception ex){
                                                log.error("e: ", ex);
                                            }

//                                            gradeRepo.save(grade);

                                        }

                                    }
                                }
                            }

                        }
                    }else if (grade.getGrade().equals("E*")){

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

                            if((!pre_ca_CalculationList.isEmpty()) && (!pre_mid_CalculationList.isEmpty())){

                                for(Calculations pre_mid_Calculation : pre_mid_CalculationList){
                                    sum = sum + Double.parseDouble(pre_mid_Calculation.getPercentage());
                                }


                                for(Calculations pre_ca_Calculation : pre_ca_CalculationList){
                                    sum = sum + Double.parseDouble(pre_ca_Calculation.getPercentage());
                                }

                                roundedSum = Math.round(sum*100)/100.00 ;

                                grade.setTotal_final_mark(String.valueOf(sum));
                                grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                MarksRangeOfGrade marksRangeOfGrade = null;
                                try{
                                    marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");

                                    if(grade.getCa_eligibility().equals("Eligible")){

                                        if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){

                                            grade.setGrade("C");
                                        } else{
                                            List<MarksRangeOfGrade> marginList = null;
                                            try{
                                                marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                                if(!marginList.isEmpty()){
                                                    grade.setGrade(marginList.get(0).getGrade());

                                                }

                                            }catch (Exception e){
                                                log.error("e: ", e);
                                            }
                                        }

                                    } else if (grade.getCa_eligibility().equals("Not eligible")) {
                                        grade.setGrade("F");

                                    } else if (grade.getCa_eligibility().equals("WH")) {
                                        grade.setGrade("WH");
                                    }


                                }catch (Exception ex){
                                    log.error("e: ", ex);
                                }

//                                gradeRepo.save(grade);

                            } else{

                                List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");
                                List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                                if((!ca_CalculationList.isEmpty()) && (!mid_CalculationList.isEmpty())){

                                    if(grade.getCa_eligibility().equals("Eligible")){

                                        for(Calculations ca_Calculation : ca_CalculationList){
                                            sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                        }


                                        for(Calculations mid_Calculation : mid_CalculationList){
                                            sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                        }

                                        roundedSum = Math.round(sum*100)/100.00 ;

                                        grade.setTotal_final_mark(String.valueOf(sum));
                                        grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                        MarksRangeOfGrade marksRangeOfGrade = null;
                                        try{
                                            marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");

                                            if(grade.getCa_eligibility().equals("Eligible")){

                                                if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){

                                                    grade.setGrade("C");
                                                } else{
                                                    List<MarksRangeOfGrade> marginList = null;
                                                    try{
                                                        marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                                        if(!marginList.isEmpty()){
                                                            grade.setGrade(marginList.get(0).getGrade());

                                                        }

                                                    }catch (Exception e){
                                                        log.error("e: ", e);
                                                    }
                                                }

                                            } else if (grade.getCa_eligibility().equals("Not eligible")) {
                                                grade.setGrade("F");

                                            } else if (grade.getCa_eligibility().equals("WH")) {
                                                grade.setGrade("WH");
                                            }


                                        }catch (Exception ex){
                                            log.error("e: ", ex);
                                        }

//                                        gradeRepo.save(grade);

                                    }

                                }

                            }

                        }


                    }else if (grade.getGrade().equals("F")){

                        List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                        List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");
                        List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");


                        double sum = 0.0 ;
                        double roundedSum = 0.0;

                        if(grade.getCa_eligibility().equals("Eligible")){
                            if(!endCalculationList.isEmpty()){

                                for(Calculations endCalculation : endCalculationList){
                                    sum = sum + Double.parseDouble(endCalculation.getPercentage());
                                }
                            }

                            if(!ca_CalculationList.isEmpty()){
                                for(Calculations ca_Calculation : ca_CalculationList){
                                    sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                }
                            }

                            if(!mid_CalculationList.isEmpty()){
                                for(Calculations mid_Calculation : mid_CalculationList){
                                    sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                }
                            }

                            roundedSum = Math.round(sum*100)/100.00 ;

                            grade.setTotal_final_mark(String.valueOf(sum));
                            grade.setTotal_rounded_mark(String.valueOf(roundedSum));


                            MarksRangeOfGrade marksRangeOfGrade = null;
                            try{
                                marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");

                                if(grade.getCa_eligibility().equals("Eligible")){

                                    if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){

                                        grade.setGrade("C");
                                    } else{
                                        List<MarksRangeOfGrade> marginList = null;
                                        try{
                                            marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                            if(!marginList.isEmpty()){
                                                grade.setGrade(marginList.get(0).getGrade());

                                            }

                                        }catch (Exception e){
                                            log.error("e: ", e);
                                        }
                                    }

                                } else if (grade.getCa_eligibility().equals("Not eligible")) {
                                    grade.setGrade("F");

                                } else if (grade.getCa_eligibility().equals("WH")) {
                                    grade.setGrade("WH");
                                }


                            }catch (Exception ex){
                                log.error("e: ", ex);
                            }

//                            gradeRepo.save(grade);

                        }

                    }else if (grade.getGrade().equals("MC")){
                        List<MarksEntity> preMidMarksList =    arMarksRepo.getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(student.getStudent_id(),course_id,pre_academic_year,"Mid");
                        List<MarksEntity> preEndMarksList =    arMarksRepo.getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(student.getStudent_id(),course_id,pre_academic_year,"End");

                        boolean isMidMc =false;
                        boolean isEndMc = false;

                        if(!preMidMarksList.isEmpty()){            //Check is there any previous MC Mid exams
                            for(MarksEntity preMidMarks : preMidMarksList){
                                if(preMidMarks.getAssignment_score().equals("MC")){
                                    isEndMc = true;
                                }
                            }
                        }

                        if(!preEndMarksList.isEmpty()){             //Check is there any previous MC End exams
                            for(MarksEntity preEndMarks : preEndMarksList){
                                if(preEndMarks.getAssignment_score().equals("MC")){
                                    isMidMc = true;
                                }
                            }
                        }

                        if(isMidMc && isEndMc){         //If both exams are MC
                            List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                            List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                            List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                            double sum = 0.0 ;
                            double roundedSum = 0.0;

                            if(grade.getCa_eligibility().equals("Eligible")){

                                if(!endCalculationList.isEmpty()){

                                    for(Calculations endCalculation : endCalculationList){
                                        sum = sum + Double.parseDouble(endCalculation.getPercentage());
                                    }
                                }

                                if(!ca_CalculationList.isEmpty()){
                                    for(Calculations ca_Calculation : ca_CalculationList){
                                        sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                    }
                                }

                                if(!mid_CalculationList.isEmpty()){
                                    for(Calculations mid_Calculation : mid_CalculationList){
                                        sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                    }
                                }

                                roundedSum = Math.round(sum*100)/100.00 ;

                                grade.setTotal_final_mark(String.valueOf(sum));
                                grade.setTotal_rounded_mark(String.valueOf(roundedSum));


                                List<MarksRangeOfGrade> marginList = null;
                                try{
                                    marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                    if(!marginList.isEmpty()){
                                        if(grade.getCa_eligibility().equals("Eligible")){
                                            grade.setGrade(marginList.get(0).getGrade());

                                        }else if (grade.getCa_eligibility().equals("Not eligible")){
                                            grade.setGrade("F");
                                        }else if(grade.getCa_eligibility().equals("WH")){
                                            grade.setGrade("WH");
                                        }
                                    }

                                }catch (Exception e){
                                    log.error("e: ", e);
                                }

//                                gradeRepo.save(grade);

                            }
                        }else{
                            if(isMidMc){

                                List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"End");
                                List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                                List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                                double sum = 0.0 ;
                                double roundedSum = 0.0;

                                if(grade.getCa_eligibility().equals("Eligible")){

                                    if(!endCalculationList.isEmpty()){

                                        for(Calculations endCalculation : endCalculationList){
                                            sum = sum + Double.parseDouble(endCalculation.getPercentage());
                                        }
                                    }

                                    if(!ca_CalculationList.isEmpty()){
                                        for(Calculations ca_Calculation : ca_CalculationList){
                                            sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                        }
                                    }

                                    if(!mid_CalculationList.isEmpty()){
                                        for(Calculations mid_Calculation : mid_CalculationList){
                                            sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                        }
                                    }

                                    roundedSum = Math.round(sum*100)/100.00 ;

                                    grade.setTotal_final_mark(String.valueOf(sum));
                                    grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                    List<MarksRangeOfGrade> marginList = null;
                                    try{
                                        marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                        if(!marginList.isEmpty()){
                                            if(grade.getCa_eligibility().equals("Eligible")){
                                                grade.setGrade(marginList.get(0).getGrade());

                                            }else if (grade.getCa_eligibility().equals("Not eligible")){
                                                grade.setGrade("F");
                                            }else if(grade.getCa_eligibility().equals("WH")){
                                                grade.setGrade("WH");
                                            }
                                        }

                                    }catch (Exception e){
                                        log.error("e: ", e);
                                    }

//                                    gradeRepo.save(grade);

                                }

                            }else{
                                if(isEndMc){

                                    List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                                    List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                                    List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                                    double sum = 0.0 ;
                                    double roundedSum = 0.0;

                                    if(grade.getCa_eligibility().equals("Eligible")) {

                                        if (!endCalculationList.isEmpty()) {

                                            for (Calculations endCalculation : endCalculationList) {
                                                sum = sum + Double.parseDouble(endCalculation.getPercentage());
                                            }
                                        }

                                        if (!ca_CalculationList.isEmpty()) {
                                            for (Calculations ca_Calculation : ca_CalculationList) {
                                                sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                            }
                                        }

                                        if (!mid_CalculationList.isEmpty()) {
                                            for (Calculations mid_Calculation : mid_CalculationList) {
                                                sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                            }
                                        }

                                        roundedSum = Math.round(sum * 100) / 100.00;

                                        grade.setTotal_final_mark(String.valueOf(sum));
                                        grade.setTotal_rounded_mark(String.valueOf(roundedSum));


                                        List<MarksRangeOfGrade> marginList = null;
                                        try{
                                            marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                            if(!marginList.isEmpty()){
                                                if(grade.getCa_eligibility().equals("Eligible")){
                                                    grade.setGrade(marginList.get(0).getGrade());

                                                }else if (grade.getCa_eligibility().equals("Not eligible")){
                                                    grade.setGrade("F");
                                                }else if(grade.getCa_eligibility().equals("WH")){
                                                    grade.setGrade("WH");
                                                }
                                            }

                                        }catch (Exception e){
                                            log.error("e: ", e);
                                        }

//                                        gradeRepo.save(grade);
                                    }

                                }
                            }
                        }




                    }
                }
            }

            List<CourseRelatedDeptEntity> courseRelatedDeptEntityList = courseRelatedDeptRepo.getDeptByCourse(course_id);
            System.out.println("Course related department details ---" + courseRelatedDeptEntityList);
            for(CourseRelatedDeptEntity courseRelatedDeptEntity : courseRelatedDeptEntityList){

                MarksApprovalLevel marksApprovalLevel = new MarksApprovalLevel();
                marksApprovalLevel.setCourse_id(course_id);
                marksApprovalLevel.setAcademic_year(academic_year);
                marksApprovalLevel.setApproval_level("finalized");
                marksApprovalLevel.setDepartment_id(courseRelatedDeptEntity.getDepartment_id());

                approvalLevelRepo.save(marksApprovalLevel);
            }



        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }



    public void calculateRoundedMarkOfSelectedStudent(String course_id, String academic_year,String student_id) {
        String pre_academic_year = reduceYear(academic_year);
        String pre_pre_academic_year =reduceYear(pre_academic_year);
        try {
//            //Get registered student for the course in the academic year (proper and repeat)
            StudentRegCourses student = studentRegCoursesRepo.getSelectedStudentRepeatDataByCID(course_id,academic_year,student_id);
//

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
                if((grade.getGrade().equals("C-")) || (grade.getGrade().equals("D")) || (grade.getGrade().equals("D+")) || (grade.getGrade().equals("E"))){
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

                                    if(grade.getCa_eligibility().equals("Eligible")){

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

                    }
                }else if (grade.getGrade().equals("E*")){

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

                        if((!pre_ca_CalculationList.isEmpty()) && (!pre_mid_CalculationList.isEmpty())){

                            for(Calculations pre_mid_Calculation : pre_mid_CalculationList){
                                sum = sum + Double.parseDouble(pre_mid_Calculation.getPercentage());
                            }


                            for(Calculations pre_ca_Calculation : pre_ca_CalculationList){
                                sum = sum + Double.parseDouble(pre_ca_Calculation.getPercentage());
                            }

                            roundedSum = Math.round(sum*100)/100.00 ;

                            grade.setTotal_final_mark(String.valueOf(sum));
                            grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                            gradeRepo.save(grade);

                        } else{

                            List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");
                            List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                            if((!ca_CalculationList.isEmpty()) && (!mid_CalculationList.isEmpty())){

                                if(grade.getCa_eligibility().equals("Eligible")){

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


                }else if (grade.getGrade().equals("F")){

                    List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                    List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");
                    List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");


                    double sum = 0.0 ;
                    double roundedSum = 0.0;

                    if(grade.getCa_eligibility().equals("Eligible")){
                        if(!endCalculationList.isEmpty()){

                            for(Calculations endCalculation : endCalculationList){
                                sum = sum + Double.parseDouble(endCalculation.getPercentage());
                            }
                        }

                        if(!ca_CalculationList.isEmpty()){
                            for(Calculations ca_Calculation : ca_CalculationList){
                                sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                            }
                        }

                        if(!mid_CalculationList.isEmpty()){
                            for(Calculations mid_Calculation : mid_CalculationList){
                                sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                            }
                        }

                        roundedSum = Math.round(sum*100)/100.00 ;

                        grade.setTotal_final_mark(String.valueOf(sum));
                        grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                        gradeRepo.save(grade);

                    }

                }else if (grade.getGrade().equals("MC")){
                    List<MarksEntity> preMidMarksList =    arMarksRepo.getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(student.getStudent_id(),course_id,pre_academic_year,"Mid");
                    List<MarksEntity> preEndMarksList =    arMarksRepo.getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(student.getStudent_id(),course_id,pre_academic_year,"End");

                    boolean isMidMc =false;
                    boolean isEndMc = false;

                    if(!preMidMarksList.isEmpty()){            //Check is there any previous MC Mid exams
                        for(MarksEntity preMidMarks : preMidMarksList){
                            if(preMidMarks.getAssignment_score().equals("MC")){
                                isEndMc = true;
                            }
                        }
                    }

                    if(!preEndMarksList.isEmpty()){             //Check is there any previous MC End exams
                        for(MarksEntity preEndMarks : preEndMarksList){
                            if(preEndMarks.getAssignment_score().equals("MC")){
                                isMidMc = true;
                            }
                        }
                    }

                    if(isMidMc && isEndMc){         //If both exams are MC
                        List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                        List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                        List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                        double sum = 0.0 ;
                        double roundedSum = 0.0;

                        if(grade.getCa_eligibility().equals("Eligible")){

                            if(!endCalculationList.isEmpty()){

                                for(Calculations endCalculation : endCalculationList){
                                    sum = sum + Double.parseDouble(endCalculation.getPercentage());
                                }
                            }

                            if(!ca_CalculationList.isEmpty()){
                                for(Calculations ca_Calculation : ca_CalculationList){
                                    sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                }
                            }

                            if(!mid_CalculationList.isEmpty()){
                                for(Calculations mid_Calculation : mid_CalculationList){
                                    sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                }
                            }

                            roundedSum = Math.round(sum*100)/100.00 ;

                            grade.setTotal_final_mark(String.valueOf(sum));
                            grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                            gradeRepo.save(grade);

                        }
                    }else{
                        if(isMidMc){

                            List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"End");
                            List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                            List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                            double sum = 0.0 ;
                            double roundedSum = 0.0;

                            if(grade.getCa_eligibility().equals("Eligible")){

                                if(!endCalculationList.isEmpty()){

                                    for(Calculations endCalculation : endCalculationList){
                                        sum = sum + Double.parseDouble(endCalculation.getPercentage());
                                    }
                                }

                                if(!ca_CalculationList.isEmpty()){
                                    for(Calculations ca_Calculation : ca_CalculationList){
                                        sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                    }
                                }

                                if(!mid_CalculationList.isEmpty()){
                                    for(Calculations mid_Calculation : mid_CalculationList){
                                        sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                    }
                                }

                                roundedSum = Math.round(sum*100)/100.00 ;

                                grade.setTotal_final_mark(String.valueOf(sum));
                                grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                gradeRepo.save(grade);

                            }

                        }else{
                            if(isEndMc){

                                List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                                List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                                List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                                double sum = 0.0 ;
                                double roundedSum = 0.0;

                                if(grade.getCa_eligibility().equals("Eligible")) {

                                    if (!endCalculationList.isEmpty()) {

                                        for (Calculations endCalculation : endCalculationList) {
                                            sum = sum + Double.parseDouble(endCalculation.getPercentage());
                                        }
                                    }

                                    if (!ca_CalculationList.isEmpty()) {
                                        for (Calculations ca_Calculation : ca_CalculationList) {
                                            sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                        }
                                    }

                                    if (!mid_CalculationList.isEmpty()) {
                                        for (Calculations mid_Calculation : mid_CalculationList) {
                                            sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                        }
                                    }

                                    roundedSum = Math.round(sum * 100) / 100.00;

                                    grade.setTotal_final_mark(String.valueOf(sum));
                                    grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                    gradeRepo.save(grade);
                                }

                            }
                        }
                    }




                }
            }


            List<CourseRelatedDeptEntity> courseRelatedDeptEntityList = courseRelatedDeptRepo.getDeptByCourse(course_id);

            for(CourseRelatedDeptEntity courseRelatedDeptEntity : courseRelatedDeptEntityList){

                MarksApprovalLevel marksApprovalLevel = new MarksApprovalLevel();
                marksApprovalLevel.setCourse_id(course_id);
                marksApprovalLevel.setAcademic_year(academic_year);
                marksApprovalLevel.setApproval_level("finalized");
                marksApprovalLevel.setDepartment_id(courseRelatedDeptEntity.getDepartment_id());

                approvalLevelRepo.save(marksApprovalLevel);
            }



        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public boolean isFinalized (String course_id, String academic_year){
        List<Grade> markList = gradeRepo.isFinalized(course_id,academic_year);
        if(markList.isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}
