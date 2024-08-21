package com.mms_backend.service;

import com.mms_backend.dto.AR.AcademicYearDetailsDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.dto.StudentRegCoursesDTO;
import com.mms_backend.entity.*;
import com.mms_backend.entity.AR.Grade;
import com.mms_backend.repository.*;
import com.mms_backend.service.AR.ARService;
import jakarta.transaction.Transactional;
import org.hibernate.resource.jdbc.spi.JdbcSessionOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CACalculationService {


    @Autowired
    private ARService arService;

    @Autowired
    private StudentRegCoursesServices studentRegCoursesServices;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private StudentMarksRepo studentMarksRepo;

    @Autowired
    private MarksRepo marksRepo;

    @Autowired
    private EvaluationCriteriaNameRepo evaluationCriteriaNameRepo;

    @Autowired
    private EvaluationCriteriaRepo evaluationCriteriaRepo;

    @Autowired
    private CalculationsRepo calculationsRepo;

    @Autowired
    private GradeRepo gradeRepo;




    private String currentAcademicYear;
    private String prev_AcademicYear;
    private String prev_prev_AcademicYear;
    private String student_id;
    private String course_id;
    private int repeatVal;
    private int total_of_CA_percentage;
    private double ca_Eli_margin;
    private int mid_percentage;
    private int assignment_name_percentage;
    private String assignment_name_store;
    private String midMarks;
    private String prev_MidMarks;
    private String prev_prev_MidMarks;
    private int current_mid_mark_percentage;
    private List<Calculations> marksCalculationsList=new ArrayList<>(); //create list to all students marks calculations
    private Calculations singleStudentMarks = new Calculations(); //create object from calculation entity
    private Grade studentGrade = new Grade(); //create object from grade entity


    @Autowired
    private TransactionAutoConfiguration.EnableTransactionManagementConfiguration.CglibAutoProxyConfiguration cglibAutoProxyConfiguration;



    public void calculateCA(String course_id) {

        this.course_id = course_id;          // get course id
        singleStudentMarks.setCourse_id(course_id);  //set course id to object

        List<AcademicYearDetailsDTO> acYears = arService.getAcademicYearDetails();
        // get academic years data
        for (AcademicYearDetailsDTO acYear : acYears) {
            currentAcademicYear = acYear.getCurrent_academic_year(); //get current academic year
            prev_AcademicYear=acYear.getPrevious_academic_year(); //get previous academic year
        }

        singleStudentMarks.setAcademic_year(currentAcademicYear); //set academic year to object

        ResponseDTO studentRegDetails = studentRegCoursesServices.getAllStudentsByCID(course_id, currentAcademicYear);

        //get prev_prev_AcademicYear
        prev_prev_AcademicYear = reduceYear(prev_AcademicYear);
        System.out.println(prev_prev_AcademicYear);


        //get Evaluation Criteria Details by course id and filter on type = 'CA'
        List<EvaluationCriteria> CAEVList = evaluationCriteriaRepo.getECbyCourseIDCA(course_id);
        for (EvaluationCriteria o : CAEVList) {
            total_of_CA_percentage += o.getPercentage();
        }
//        System.out.println("Total of CA percentage "+total_of_CA_percentage);

        // CA eligibility margin calculation
        ca_Eli_margin = (total_of_CA_percentage/2)-0.5;

        //get mid percentage on current academic year
        List<EvaluationCriteria> midEvDetails = evaluationCriteriaRepo.getEBMIDbyCourseID(course_id);
        for (EvaluationCriteria ev : midEvDetails) {
            current_mid_mark_percentage = ev.getPercentage();
        }



        List<StudentRegCoursesDTO> studentRegCoursesDTOS = (List<StudentRegCoursesDTO>) studentRegDetails.getContent();     // type cast and store student details in a list

        for (StudentRegCoursesDTO studentRegCoursesDTO : studentRegCoursesDTOS) {    // iterate through the list



            student_id = studentRegCoursesDTO.getStudent_id();            // get student id
            System.out.println("student_id :" + student_id);


            repeatVal = studentRegCoursesDTO.getRepeat();
            String student_CA_Eligibility = "";
            System.out.println(repeatVal);
            if (repeatVal == 1) {
                singleStudentMarks.setStudent_id(student_id); //set student id to object
                // System.out.println("student_id  Repeat:" + student_id);
               StudentMarks studentMarks= studentMarksRepo.findMarksByCS(course_id,student_id);

                String ca_eligibility="";
               if (studentMarks!=null){
                   ca_eligibility=studentMarks.getCa_eligibility();
               }

                System.out.println("ca eligibility "+ca_eligibility);

                if(ca_eligibility.equals("Eligible")){

                    List<EvaluationCriteriaNameEntity> list=evaluationCriteriaNameRepo.getMCMidName(student_id,course_id,prev_AcademicYear);

                    List<EvaluationCriteria> midEVList = evaluationCriteriaRepo.getEBMIDbyCourseID(course_id);

                    for (EvaluationCriteriaNameEntity o : list) {
                        for (EvaluationCriteria ev : midEVList) {
                            assignment_name_percentage = ev.getPercentage();
                            System.out.println(" "+ev.getPercentage());

                            assignment_name_store = o.getAssignment_name();
                            System.out.println(o.getAssignment_name());
                        }
                    }

                    //getting mid marks from marks table
                    List<MarksEntity> midMarksDetails = marksRepo.getMarksByStuIDCourseIDAssignmentName(student_id,course_id,assignment_name_store,  currentAcademicYear);
                    for (MarksEntity midMarksDetail : midMarksDetails) {
                        midMarks = midMarksDetail.getAssignment_score();  //declaring mid marks
                        System.out.println("mid marks " + midMarks);
                    }

                    //getting mid marks from marks table
                    List<MarksEntity> prev_MidMarksDetails = marksRepo.getMarksByStuIDCourseIDAssignmentName(student_id,course_id,assignment_name_store,  prev_AcademicYear);
                    for (MarksEntity mid : prev_MidMarksDetails) {
                        prev_MidMarks = mid.getAssignment_score();  //declaring prev_mid marks
                        System.out.println("prev_mid marks " + prev_MidMarks);
                    }


                    //getting mid marks from marks table
                    List<MarksEntity> prev_Prev_MidMarksDetails = marksRepo.getMarksByStuIDCourseIDAssignmentName(student_id,course_id,assignment_name_store,  prev_prev_AcademicYear);
                    for (MarksEntity mid : prev_Prev_MidMarksDetails) {
                        prev_prev_MidMarks = mid.getAssignment_score();  //initialization prev_prev_mid marks
                        System.out.println("prev_prev_mid marks " + prev_prev_MidMarks);
                    }

                    double calculated_old_ca_total = 0;
                    double current_mid_mark = 0;
                    double calculated_current_mid_mark_as_percentage = 0;
                    double percentage_of_current_mid_mark = 0;

                    //get mid-marks on current academic year
                    List<MarksEntity> currentMidMarks = marksRepo.getMarksByStuIDCourseIDAssignmentName(student_id,course_id,assignment_name_store,  currentAcademicYear);
                    for (MarksEntity mid : currentMidMarks) {

                        singleStudentMarks.setEvaluation_criteria_id(mid.getEvaluation_criteria_id());

                        //checking is Mid-marks was 'AB' again
                        if (mid.getAssignment_score().equals("AB")){
                            current_mid_mark += 0;
                            student_CA_Eligibility = "WH"; //initialization student CA eligibility
                        }else {
                            current_mid_mark += Double.parseDouble(mid.getAssignment_score());  //initialization current mid marks
                        }
                        calculated_old_ca_total += current_mid_mark; //initialization current mid marks
                        System.out.println("current mid marks " + current_mid_mark);

                        singleStudentMarks.setMark(String.valueOf(current_mid_mark)); //set current mid marks to object

                        marksCalculationsList.add(singleStudentMarks); //add object to list
                    }

                    percentage_of_current_mid_mark = (calculated_old_ca_total * current_mid_mark_percentage) / 100;

                    singleStudentMarks.setPercentage(String.valueOf(percentage_of_current_mid_mark)); //set percentage of current mid marks to object
                    calculated_current_mid_mark_as_percentage = percentage_of_current_mid_mark;
                    calculated_old_ca_total = calculated_current_mid_mark_as_percentage;

                    System.out.println("calculated current mid mark as percentage : " +calculated_current_mid_mark_as_percentage);




                    //checking mc and get old ca marks
                    if (prev_prev_MidMarks != null && prev_prev_MidMarks.equals("MC")) {
                        if (prev_MidMarks != null && prev_MidMarks.equals("MC")) {
                            List<Object[]> getOLD_CAs = calculationsRepo.getOLDCAByStuIDCourseID_AY(student_id,course_id,prev_prev_AcademicYear);
                            for (Object[] oldCA : getOLD_CAs) {
                                System.out.println("pre pre data : "+ Double.parseDouble(oldCA[0].toString()));
                                System.out.println("percentage : "+ Double.parseDouble(oldCA[1].toString()));

                                Double mark = Double.parseDouble(oldCA[0].toString()); // Cast to String first, then parse to Double
                                Double percentage = Double.parseDouble(oldCA[1].toString()); // Cast to String first, then parse to Double

                                calculated_old_ca_total += (mark * percentage) / 100;





                            }
                        }
                    } else if (prev_prev_MidMarks == null && prev_MidMarks != null && prev_MidMarks.equals("MC")) {
                        List<Object[]> getOLD_CAs = calculationsRepo.getOLDCAByStuIDCourseID_AY(student_id,course_id,prev_AcademicYear);
                        for (Object[] oldCA : getOLD_CAs) {
                            System.out.println("pre data : "+ Double.parseDouble(oldCA[0].toString()));
                            System.out.println("percentage : "+ Double.parseDouble(oldCA[1].toString()));

                            Double mark = Double.parseDouble(oldCA[0].toString()); // Cast to String first, then parse to Double
                            Double percentage = Double.parseDouble(oldCA[1].toString()); // Cast to String first, then parse to Double

                            calculated_old_ca_total += (mark * percentage) / 100;
                        }
                    }

                    System.out.println("calculated old ca total : "+calculated_old_ca_total);

                    //set eligibility
                    if (student_CA_Eligibility.equals("WH")) {
                        student_CA_Eligibility = "WH";
                    } else{
                        if (calculated_old_ca_total >= ca_Eli_margin) {
                            student_CA_Eligibility = "Eligible";
                        } else {
                            student_CA_Eligibility = "Not Eligible";
                        }
                    }

                    studentGrade = gradeRepo.getGradeDetailsBY_SIID_CID(student_id,course_id);

                    studentGrade.setStudent_id(student_id);
                    studentGrade.setCourse_id(course_id);
                    studentGrade.setTotal_ca_mark(String.valueOf(calculated_old_ca_total));
                    studentGrade.setCa_eligibility(student_CA_Eligibility);

                    gradeRepo.save(studentGrade);


                }
                else if(ca_eligibility.equals("Not Eligible")){


                }
            }
            else {

            }





        }

        System.out.println("Total calculation records :" + marksCalculationsList);
        calculationsRepo.saveAll(marksCalculationsList);

    }


    public static String reduceYear(String years){

        String[] temp_Years_part = years.split("-");
        int year1 = Integer.parseInt(temp_Years_part[0]) -1;
        int year2 = Integer.parseInt(temp_Years_part[1]) -1;

        return year1 + "-" + year2;
    }
}