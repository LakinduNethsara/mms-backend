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

                    try{
                        gradeRepo.save(grade);      //Save grade details in Database
                    } catch(Exception err){
                        System.out.println("Error is occurred while saving saving proper student grade details ");
                    }


                }else{
                    //Scenario for repeat-----------------------------------------------------------------------------------------
                    System.out.println("This is repeat-------------------------------");
                    if((grade.getGrade().equals("C-")) || (grade.getGrade().equals("D")) || (grade.getGrade().equals("D+")) || (grade.getGrade().equals("E"))){
////
                        List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");     //Get current(new) end marks
                        List<Calculations> pre_ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");      //Get perevious year CA Marks
                        List<Calculations> pre_mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"Mid");    //Get previous year Mid Marks
                        double sum = 0.0 ;  // variable to store total_final_marks
                        double roundedSum = 0.0;    //Variable to store total_rounded_marks
//
                        if(!endCalculationList.isEmpty()){
                            for(Calculations endCalculation : endCalculationList){      //Add all end marks to total_final_marks
                                sum = sum + Double.parseDouble(endCalculation.getPercentage());
                            }
//
                            if((!pre_ca_CalculationList.isEmpty()) && (!pre_mid_CalculationList.isEmpty())){        //check student have Mid and CA for previous year
//

                                for(Calculations pre_mid_Calculation : pre_mid_CalculationList){        //get all previous year mid marks and add to the total_final_marks
                                    sum = sum + Double.parseDouble(pre_mid_Calculation.getPercentage());
                                }

                                for(Calculations pre_ca_Calculation : pre_ca_CalculationList){      //get all previous year CA marks and add to the total_final_marks
                                    sum = sum + Double.parseDouble(pre_ca_Calculation.getPercentage());
                                }

                                roundedSum = Math.round(sum*100)/100.00 ;       //Calculate roundad marks

                                grade.setTotal_final_mark(String.valueOf(sum));     //set total final mark
                                grade.setTotal_rounded_mark(String.valueOf(roundedSum));    //set total rounded mark

                                MarksRangeOfGrade marksRangeOfGrade = null;
                                try{
                                    marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");    //get margin for the garade "C"

                                    if(grade.getOverall_eligibility().equals("Eligible")){          //Check is the student pass the CA

                                        if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){   //Check is studetn can get a C grade

                                            grade.setGrade("C");        //Set grade
                                        } else{         //If studetn can not get C grade
                                            List<MarksRangeOfGrade> marginList = null;
                                            try{
                                                marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));     //Get possible grade accoding to his score
                                                if(!marginList.isEmpty()){
                                                    grade.setGrade(marginList.get(0).getGrade());   //Set possible grade

                                                }

                                            }catch (Exception e){
                                                log.error("e: ", e);
                                            }
                                        }

                                    } else if (grade.getOverall_eligibility().equals("Not eligible")) {     //if studen CA not eligible
                                        grade.setGrade("F");        //Grade hould be F

                                    } else if (grade.getOverall_eligibility().equals("WH")) {       //If he has WH for CA
                                        grade.setGrade("WH");           //Grade also should WH
                                    }


                                }catch (Exception ex){
                                    log.error("e: ", ex);
                                }


                                try{
                                    gradeRepo.save(grade);      //Save grade details in Database
                                } catch(Exception err){
                                    System.out.println("Error is occurred while saving saving repeat student grade details ");
                                }




                            }else {     //If student dont have both CA and Mid marks in previous. Then need to check for the year befor last year CA and Mid marks

                                List<Calculations> pre_pre_ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_pre_academic_year,"CA");      //Get one year befor last year CA marks
                                List<Calculations> pre_pre_mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_pre_academic_year,"Mid");    //Get one year befor last year Mid marks

                                if((!pre_pre_ca_CalculationList.isEmpty()) && (!pre_pre_mid_CalculationList.isEmpty())){    //check is the student has both Mid and CA marks in one year before last year

                                    for(Calculations pre_pre_ca_Calculation : pre_pre_ca_CalculationList){          //add all the ca marks to total_final_mark
                                        sum = sum + Double.parseDouble(pre_pre_ca_Calculation.getPercentage());
                                    }


                                    for(Calculations pre_pre_mid_Calculation : pre_pre_mid_CalculationList){        //add all the mid marks to total_final_mark
                                        sum = sum + Double.parseDouble(pre_pre_mid_Calculation.getPercentage());
                                    }

                                    roundedSum = Math.round(sum*100)/100.00 ;       //calculate the rounded mark

                                    grade.setTotal_final_mark(String.valueOf(sum));     //set the final mark to grade entity
                                    grade.setTotal_rounded_mark(String.valueOf(roundedSum));    //set the rounded marks to grade entity


                                    MarksRangeOfGrade marksRangeOfGrade = null;
                                    try{
                                        marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");        //get the margin of the grade "C"

                                        if(grade.getOverall_eligibility().equals("Eligible")){   //Chek is the student pass the CA

                                            if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){       //is the studen has marks more than the "C"

                                                grade.setGrade("C");        //Give "C" grade
                                            } else{     //If studen has marks less than grade "C"
                                                List<MarksRangeOfGrade> marginList = null;
                                                try{
                                                    marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));     //Get possible grade
                                                    if(!marginList.isEmpty()){
                                                        grade.setGrade(marginList.get(0).getGrade());       //Set the possible grade as the grade for student

                                                    }

                                                }catch (Exception e){
                                                    log.error("e: ", e);
                                                }
                                            }

                                        } else if (grade.getOverall_eligibility().equals("Not eligible")) {      //If studen not eligible CA
                                            grade.setGrade("F");        //Grade should be "F"

                                        } else if (grade.getOverall_eligibility().equals("WH")) {        //If studen has WH for CA
                                            grade.setGrade("WH");       //Grade should be "WH"
                                        }


                                    }catch (Exception ex){
                                        log.error("e: ", ex);
                                    }

                                    try{
                                        gradeRepo.save(grade);      //Save grade details in Database
                                    } catch(Exception err){
                                        System.out.println("Error is occurred while saving saving repeat student grade details ");
                                    }  //Save the grade details in the back end


                                }else{      //If he writes all the exams as a repeat student after 2 years

                                    List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");      //Get current year CA marks as repeat student (Need to write ca again )
                                    List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");    //Get current year Mid marks as repeat student (Need to write ca again )

                                    if((!ca_CalculationList.isEmpty()) && (!mid_CalculationList.isEmpty())){        //Check is he has both CA and MID marks

                                        for(Calculations ca_Calculation : ca_CalculationList){      //Add all CA marks to final marks storing variable
                                            sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                        }


                                        for(Calculations mid_Calculation : mid_CalculationList){        //Add all Mid marks to the final mark storing variable
                                            sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                        }

                                        roundedSum = Math.round(sum*100)/100.00 ;       //Calculate rounded final marks

                                        grade.setTotal_final_mark(String.valueOf(sum));     //Set final mark to grade entity
                                        grade.setTotal_rounded_mark(String.valueOf(roundedSum));        //Set rounded mark to grade entity


                                        if(grade.getOverall_eligibility().equals("Eligible")){      //is student has CA eligible


                                            MarksRangeOfGrade marksRangeOfGrade = null;
                                            try{
                                                marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");        //Get the margine of Grade "C"

                                                if(grade.getOverall_eligibility().equals("Eligible")){

                                                    if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){       //Is studen has marks mor than or equal "C"

                                                        grade.setGrade("C");    //grade should be "C"
                                                    } else{     //If student has marks less than "C"
                                                        List<MarksRangeOfGrade> marginList = null;
                                                        try{
                                                            marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));     //Get possible grade
                                                            if(!marginList.isEmpty()){
                                                                grade.setGrade(marginList.get(0).getGrade());       //Set possible grade to grade entity
                                                            }

                                                        }catch (Exception e){
                                                            log.error("e: ", e);
                                                        }
                                                    }

                                                }


                                            }catch (Exception ex){
                                                log.error("e: ", ex);
                                            }

//

                                        }else if (grade.getCa_eligibility().equals("Not eligible")) {       //If student CA not eligible
                                            grade.setGrade("F");        //Grade should be "F"

                                        } else if (grade.getCa_eligibility().equals("WH")) {        //If student has "WH" for CA
                                            grade.setGrade("WH");       //grade should be "WH"
                                        }

                                    }

                                    try{
                                        gradeRepo.save(grade);      //Save grade details in Database
                                    } catch(Exception err){
                                        System.out.println("Error is occurred while saving saving repeat student grade details ");
                                    }
                                }
                            }

                        }
                    }else if (grade.getGrade().equals("E*")){

                        List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");     //get current academic year end marks
                        List<Calculations> pre_ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");      //get pervious year CA marks
                        List<Calculations> pre_mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"Mid");        //get previous year MID marks

                        double sum = 0.0 ;      //variable to store total_final_mark
                        double roundedSum = 0.0;        //variable to store total_rounded_marks

                        if(!endCalculationList.isEmpty()){

                            for(Calculations endCalculation : endCalculationList){      //add all end marks to the the variable
                                sum = sum + Double.parseDouble(endCalculation.getPercentage());
                            }

                            if((!pre_ca_CalculationList.isEmpty()) && (!pre_mid_CalculationList.isEmpty())){        //if student has both mid and CA marks in previous year

                                for(Calculations pre_mid_Calculation : pre_mid_CalculationList){        //add all MID marks to the total mark variable
                                    sum = sum + Double.parseDouble(pre_mid_Calculation.getPercentage());
                                }


                                for(Calculations pre_ca_Calculation : pre_ca_CalculationList){      //add all CA marks to the final marks variable
                                    sum = sum + Double.parseDouble(pre_ca_Calculation.getPercentage());
                                }

                                roundedSum = Math.round(sum*100)/100.00 ;       //calculate rounded marks

                                grade.setTotal_final_mark(String.valueOf(sum));     //set final marks to the grade entity
                                grade.setTotal_rounded_mark(String.valueOf(roundedSum));        //set total rounded marks to grade entity

                                MarksRangeOfGrade marksRangeOfGrade = null;
                                try{
                                    marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");        //get margin of grade "C"

                                    if(grade.getOverall_eligibility().equals("Eligible")){       //if student has CA eligible

                                        if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){       //If student has marks more than "C"

                                            grade.setGrade("C");    //Grade should be "C"

                                        } else{     //If student has marks less than garade "C"
                                            List<MarksRangeOfGrade> marginList = null;
                                            try{
                                                marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));     //Get possible grade
                                                if(!marginList.isEmpty()){
                                                    grade.setGrade(marginList.get(0).getGrade());       //Set possible grade

                                                }

                                            }catch (Exception e){
                                                log.error("e: ", e);
                                            }
                                        }

                                    } else if (grade.getCa_eligibility().equals("Not eligible")) {      //If student not eligible for CA
                                        grade.setGrade("F");        //Grade should be "F"

                                    } else if (grade.getCa_eligibility().equals("WH")) {        //if student has "WH"
                                        grade.setGrade("WH");       //Grade should be "WH"
                                    }


                                }catch (Exception ex){
                                    log.error("e: ", ex);
                                }

                                try{
                                    gradeRepo.save(grade);      //Save grade details in Database
                                } catch(Exception err){
                                    System.out.println("Error is occurred while saving saving repeat student grade details ");
                                }      //Save grade details to database

                            } else{

                                List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");      //Get current CA
                                List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");    //Get current Mid

                                if((!ca_CalculationList.isEmpty()) && (!mid_CalculationList.isEmpty())){        //if student has CA and MID marks in this academic year

                                    for(Calculations ca_Calculation : ca_CalculationList){
                                        sum = sum + Double.parseDouble(ca_Calculation.getPercentage());
                                    }


                                    for(Calculations mid_Calculation : mid_CalculationList){
                                        sum = sum + Double.parseDouble(mid_Calculation.getPercentage());
                                    }

                                    roundedSum = Math.round(sum*100)/100.00 ;

                                    grade.setTotal_final_mark(String.valueOf(sum));
                                    grade.setTotal_rounded_mark(String.valueOf(roundedSum));

                                    if(grade.getOverall_eligibility().equals("Eligible")){       //If student has CA eligible



                                        MarksRangeOfGrade marksRangeOfGrade = null;
                                        try{
                                            marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");        //Get the margin of Grade "C"

                                            if(grade.getOverall_eligibility().equals("Eligible")){       //If student has pass CA

                                                if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){       //If student has marks more than Grade "C

                                                    grade.setGrade("C");        //Grade should be "C"
                                                } else{     //If student have marks less than "C"
                                                    List<MarksRangeOfGrade> marginList = null;
                                                    try{
                                                        marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));     //Get the possible grade
                                                        if(!marginList.isEmpty()){
                                                            grade.setGrade(marginList.get(0).getGrade());       //Set the possible grade
                                                        }

                                                    }catch (Exception e){
                                                        log.error("e: ", e);
                                                    }
                                                }

                                            }


                                        }catch (Exception ex){
                                            log.error("e: ", ex);
                                        }


                                    }
                                    else if (grade.getCa_eligibility().equals("Not eligible")) {        //Is student has not eligible CA
                                        grade.setGrade("F");        //Grade should be "F"

                                    } else if (grade.getCa_eligibility().equals("WH")) {        //If student has "WH" for CA
                                        grade.setGrade("WH");       //grade should be "WH"
                                    }
                                    try{
                                        gradeRepo.save(grade);      //Save grade details in Database
                                    } catch(Exception err){
                                        System.out.println("Error is occurred while saving saving repeat student grade details ");
                                    }      //Save grade details in Database

                                }
                            }

                        }


                    }else if (grade.getGrade().equals("F")){

                        List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");     //Get end exam marks calculations records from marks calculation for current academic year
                        List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"CA");      //Get CA marks calculations records from marks calculation for current academic year
                        List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");    //Get Mid exam marks calculations records from marks calculation for current academic year


                        double sum = 0.0 ;
                        double roundedSum = 0.0;

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

                        if(grade.getOverall_eligibility().equals("Eligible")){

                            MarksRangeOfGrade marksRangeOfGrade = null;
                            try{
                                marksRangeOfGrade = marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,"C");


                                    if(marksRangeOfGrade.getMargin_of_grade() <= roundedSum){

                                        grade.setGrade("C");
                                    }
                                    else {
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




                            }catch (Exception ex){
                                log.error("e: ", ex);
                            }



                        }
                        else if (grade.getCa_eligibility().equals("Not eligible")) {
                            grade.setGrade("F");

                        } else if (grade.getCa_eligibility().equals("WH")) {
                            grade.setGrade("WH");
                        }

                        try{
                            gradeRepo.save(grade);      //Save grade details in Database
                        } catch(Exception err){
                            System.out.println("Error is occurred while savin saving repeat student grade details ");
                        }

                    }else if (grade.getGrade().equals("MC")){       //scenario for grade WH student
                        List<MarksEntity> preMidMarksList =    arMarksRepo.getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(student.getStudent_id(),course_id,pre_academic_year,"Mid");
                        List<MarksEntity> preEndMarksList =    arMarksRepo.getSelectedStudentSelectedExamMarksBySelectedCourseAndSelectedAcademicYear(student.getStudent_id(),course_id,pre_academic_year,"End");

                        boolean isMidMc =false;
                        boolean isEndMc = false;

                        if(!preMidMarksList.isEmpty()){            //Check is there any previous MC Mid exams
                            for(MarksEntity preMidMarks : preMidMarksList){
                                if(preMidMarks.getAssignment_score().equals("MC")){
                                    isMidMc = true;
                                }
                            }
                        }

                        if(!preEndMarksList.isEmpty()){             //Check is there any previous MC End exams
                            for(MarksEntity preEndMarks : preEndMarksList){
                                if(preEndMarks.getAssignment_score().equals("MC")){
                                    isEndMc = true;
                                }
                            }
                        }

                        if(isMidMc && isEndMc){         //If both exams are MC
                            List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                            List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                            List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");



                            double sum = 0.0 ;
                            double roundedSum = 0.0;

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

                            if(grade.getOverall_eligibility().equals("Eligible")){

                                List<MarksRangeOfGrade> marginList = null;
                                try{
                                    marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                    if(!marginList.isEmpty()){
                                        if(grade.getOverall_eligibility().equals("Eligible")){
                                            grade.setGrade(marginList.get(0).getGrade());

                                        }
                                    }

                                }catch (Exception e){
                                    log.error("e: ", e);
                                }

                            }
                            else if (grade.getOverall_eligibility().equals("Not eligible")){
                                grade.setGrade("F");
                            }else if(grade.getOverall_eligibility().equals("WH")){
                                grade.setGrade("WH");
                            }

                            try{
                                gradeRepo.save(grade);      //Save grade details in Database
                            } catch(Exception err){
                                System.out.println("Error is occurred while saving saving repeat student grade details ");
                            }

                        }else{
                            if(isMidMc){

                                List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"End");
                                List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                                List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"Mid");

                                double sum = 0.0 ;
                                double roundedSum = 0.0;

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


                                if(grade.getOverall_eligibility().equals("Eligible")){


                                    List<MarksRangeOfGrade> marginList = null;
                                    try{
                                        marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                        if(!marginList.isEmpty()){
                                            if(grade.getOverall_eligibility().equals("Eligible")){
                                                grade.setGrade(marginList.get(0).getGrade());

                                            }
                                        }

                                    }catch (Exception e){
                                        log.error("e: ", e);
                                    }



                                }
                                else if (grade.getCa_eligibility().equals("Not eligible")){
                                    grade.setGrade("F");
                                }else if(grade.getCa_eligibility().equals("WH")){
                                    grade.setGrade("WH");
                                }

                                try{
                                    gradeRepo.save(grade);      //Save grade details in Database
                                } catch(Exception err){
                                    System.out.println("Error is occurred while saving saving repeat student grade details ");
                                }


                            }else{
                                if(isEndMc){

                                    List<Calculations> endCalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,academic_year,"End");
                                    List<Calculations> ca_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"CA");
                                    List<Calculations> mid_CalculationList = calculationsRepo.getCalculationData(student.getStudent_id(),course_id,pre_academic_year,"Mid");

                                    double sum = 0.0 ;
                                    double roundedSum = 0.0;

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

                                    if(grade.getCa_eligibility().equals("Eligible")) {

                                        List<MarksRangeOfGrade> marginList = null;
                                        try{
                                            marginList = marksRangeOfCourseRepo.getGradeForProper(course_id,academic_year, String.valueOf(roundedSum));
                                            if(!marginList.isEmpty()){
                                                if(grade.getCa_eligibility().equals("Eligible")){
                                                    grade.setGrade(marginList.get(0).getGrade());

                                                }
                                            }

                                        }catch (Exception e){
                                            log.error("e: ", e);
                                        }

//
                                    }else if (grade.getCa_eligibility().equals("Not eligible")){
                                        grade.setGrade("F");
                                    }else if(grade.getCa_eligibility().equals("WH")){
                                        grade.setGrade("WH");
                                    }
                                    try{
                                        gradeRepo.save(grade);      //Save grade details in Database
                                    } catch(Exception err){
                                        System.out.println("Error is occurred while saving saving repeat student grade details ");
                                    }
                                }
                            }
                        }

//                        try{
//                            gradeRepo.save(grade);
//                        } catch(Exception err){
//                            System.out.println("Error occured while savin saving repeat MC student grade detials ");
//                        }


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
