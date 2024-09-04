package com.mms_backend.service;

import com.mms_backend.dto.MarksEditLogDTO;
import com.mms_backend.dto.ObjectDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.entity.*;
import com.mms_backend.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MarkSheetService
{

    @Autowired
    private EvaluationCriteriaRepo evaluationCriteriaRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private MarksRepo marksRepo;

    @Autowired
    private CalculationsRepo calculationsRepo;

    @Autowired
    private StudentMarksRepo studentMarksRepo;

    @Autowired
    private StudentRegCoursesRepo studentRegCoursesRepo;

    @Autowired
    private FinalSelectionRepo finalSelectionRepo;
    List<EvaluationCriteria> evaluationCriteriaList;
    List<ObjectDTO> dataList=new ArrayList<>();
    List<MarksEntity> marksEntityList;
    List<Calculations> calculations;
    List<StudentMarks> studentMarksList;
    List<StudentRegCourses> studentRegCoursesList;
    @Autowired
    private UserRepo studentRepo;

    @Autowired
    private MarksEditLogRepo marksEditLogRepo;
    Boolean CA=false;
    Boolean end=false;

    String description;

    public void getEvaluationCriteria(String course_id)
    {
        evaluationCriteriaList=evaluationCriteriaRepo.getEvaluationCriteria(course_id);
    }


    public void getAllScoreByCourseId(String course_id,String academic_year){
        marksEntityList=marksRepo.findStudentMarksByCourseID(course_id,academic_year);
    }

    public void getMarksCalculations(String course_id,String academic_year)
    {
        calculations=calculationsRepo.getStudentsCalculationresults(course_id,academic_year);
    }

    public void getMarksbyC(String course_id,String academic_year)
    {
        studentMarksList=studentMarksRepo.findMarksByCourse(course_id, academic_year);
    }


    public void getStudentsByCourseCode(String course_id,int type,String academicYear,String department)
    {
        studentRegCoursesList=studentRegCoursesRepo.getStudentsbyCourseCodeandRepeat(course_id,type,academicYear,department);
    }

    public List<StudentData> getData(String course_id,int type,String academicYear,String department) {

        List<StudentData> list=new ArrayList<>();
        getEvaluationCriteria(course_id);
        getAllScoreByCourseId(course_id,academicYear);
        getMarksCalculations(course_id,academicYear);
        getMarksbyC(course_id,academicYear);
        getStudentsByCourseCode(course_id,type,academicYear,department);

        for (StudentRegCourses student : studentRegCoursesList) {
            List<ObjectDTO> caMarks = new ArrayList<>();
            List<ObjectDTO> endMarks=new ArrayList<>();

            for (EvaluationCriteria object : evaluationCriteriaList) {
                if (object.getType().equals("CA")) {
                    CA = true;

                    if (object.getNo_of_conducted() > 1) {
                        for (MarksEntity markEntity : marksEntityList) {
                            if (markEntity.getStudent_id().equals(student.getStudent_id()) && markEntity.getEvaluation_criteria_id().equals(object.getEvaluationcriteria_id())) {
                                caMarks.add(new ObjectDTO(markEntity.getAssignment_name(), markEntity.getAssignment_score()!=null?markEntity.getAssignment_score():"-","score"));
                            }
                        }
                        for (Calculations calculation : calculations) {
                            if (calculation.getStudent_id().equals(student.getStudent_id()) && calculation.getEvaluation_criteria_id().equals(object.getEvaluationcriteria_id())) {
                                description="Best "+object.getNo_of_taken()+" average";
                                caMarks.add(new ObjectDTO(description, calculation.getMark()!=null?calculation.getMark():"-","average"));
                            }
                        }
                    } else {
                        for (MarksEntity markEntity : marksEntityList) {
                            if (markEntity.getStudent_id().equals(student.getStudent_id()) && markEntity.getEvaluation_criteria_id().equals(object.getEvaluationcriteria_id())) {
                                caMarks.add(new ObjectDTO(markEntity.getAssignment_name(), markEntity.getAssignment_score()!=null?markEntity.getAssignment_score():"-","score"));
                            }
                        }
                    }
                    for (Calculations calculation : calculations) {
                        if (calculation.getStudent_id().equals(student.getStudent_id()) && calculation.getEvaluation_criteria_id().equals(object.getEvaluationcriteria_id())) {
                            caMarks.add(new ObjectDTO(object.getPercentage() + "% from " + object.getAssessment_type(), calculation.getPercentage()!=null?calculation.getPercentage():"-","percentage"));
                        }
                    }
                }
            }





            for (EvaluationCriteria object : evaluationCriteriaList) {
                if (object.getType().equals("End")) {
                    end = true;

                    for (MarksEntity markEntity : marksEntityList) {
                        if (markEntity.getStudent_id().equals(student.getStudent_id()) && markEntity.getEvaluation_criteria_id().equals(object.getEvaluationcriteria_id())) {
                            endMarks.add(new ObjectDTO(markEntity.getAssignment_name(), markEntity.getAssignment_score()!=null?markEntity.getAssignment_score():"-" ,"score"));

                            for (Calculations calculation : calculations) {
                                if (calculation.getStudent_id().equals(student.getStudent_id()) && calculation.getEvaluation_criteria_id().equals(markEntity.getEvaluation_criteria_id()) && !markEntity.getAssignment_name().equalsIgnoreCase("1st Marking") &&  !markEntity.getAssignment_name().equalsIgnoreCase("2nd Marking")  ) {
                                    endMarks.add(new ObjectDTO(object.getPercentage() + "% from " + object.getAssessment_type(), calculation.getPercentage()!=null?calculation.getPercentage():"-","percentage"));
                                }
                            }
                        }
                    }
                }

            }


            User studentDetails=studentRepo.getStudentDetailsByStudentID((student.getStudent_id()));

            StudentData newstudent = new StudentData();
            newstudent.setStudent_id(student.getStudent_id());
            newstudent.setStudent_name(studentDetails.getName_with_initials());
            newstudent.setCourse_id(course_id);
            newstudent.setCa(caMarks);
            newstudent.setEnd(endMarks);
            newstudent.setRepeat(student.getIs_repeat());




            for (StudentMarks studentmark : studentMarksList) {
                if (studentmark.getStudent_id().equals(student.getStudent_id())) {
                    newstudent.setTotal_final_marks(studentmark.getTotal_final_mark() != null ? studentmark.getTotal_final_mark() : "-");
                    newstudent.setTotal_rounded_marks(studentmark.getTotal_rounded_mark() != null ? studentmark.getTotal_rounded_mark() : "-");
                    newstudent.setGrade(studentmark.getGrade() != null ? studentmark.getGrade() : "-");
                    newstudent.setGpv(studentmark.getGpv() != null ? studentmark.getGpv() : "-");
                    if (CA) {
                        newstudent.setTotal_ca_mark(studentmark.getTotal_ca_mark()!= null ? studentmark.getTotal_ca_mark() : "-");
                        newstudent.setCa_eligibility(studentmark.getCa_eligibility()!=null?  studentmark.getCa_eligibility():"-" );
                    }else
                    {
                        newstudent.setTotal_ca_mark("-");
                        newstudent.setCa_eligibility("-");
                    }
                }
            }
            list.add(newstudent);
        }

        return list;
    }

    public void updateEndMarks(StudentData studentData, MarksEditLogDTO marksEditLog, String academicYear) {
        double firstMark = 0;
        double secondMark = 0;

        try {
            for (ObjectDTO object : studentData.getEnd()) {
                if ("score".equals(object.getDescription())) {
                    if ("1st Marking".equalsIgnoreCase(object.getKey()) ||
                            "2nd Marking".equalsIgnoreCase(object.getKey()) ||
                            "Final Marks".equalsIgnoreCase(object.getKey())) {

                        String selected = finalSelectionRepo.getSelected(studentData.getCourse_id(), academicYear);
                        System.out.println("Selected Criteria: " + selected);

                        if ("1st Marking".equalsIgnoreCase(object.getKey())) {

                            marksRepo.updateEndMarks(object.getValue(), studentData.getStudent_id(),
                                    studentData.getCourse_id(), object.getKey(), academicYear);
                            firstMark = Double.parseDouble(object.getValue());
                            System.out.println(object.getKey() + ": " + firstMark);

                            if ("1st Marking".equalsIgnoreCase(selected)) {
                                marksRepo.updateEndMarks(String.valueOf(firstMark), studentData.getStudent_id(),
                                        studentData.getCourse_id(), "Final Marks", academicYear);
                            }
                        } else if ("2nd Marking".equalsIgnoreCase(object.getKey())) {
                            secondMark = Double.parseDouble(object.getValue());
                            System.out.println(object.getKey() + ": " + secondMark);

                            marksRepo.updateEndMarks(object.getValue(), studentData.getStudent_id(),
                                    studentData.getCourse_id(), object.getKey(), academicYear);

                            if ("2nd Marking".equalsIgnoreCase(selected)) {
                                marksRepo.updateEndMarks(String.valueOf(secondMark), studentData.getStudent_id(),
                                        studentData.getCourse_id(), "Final Marks", academicYear);
                            }
                        } else if ("Final Marks".equalsIgnoreCase(object.getKey())) {
                            if ("average".equalsIgnoreCase(selected)) {
                                double averageMark = (firstMark + secondMark) / 2;
                                marksRepo.updateEndMarks(String.valueOf(averageMark), studentData.getStudent_id(),
                                        studentData.getCourse_id(), "Final Marks", academicYear);
                            }
                        }

                    } else {
                        marksRepo.updateEndMarks(object.getValue(), studentData.getStudent_id(),
                                studentData.getCourse_id(), object.getKey(), academicYear);
                    }
                }
            }
            marksEditLogRepo.save(modelMapper.map(marksEditLog, Marks_edit_log.class));

        } catch (Exception e) {
            System.err.println("Error updating marks: " + e.getMessage());
        }


        public void updateCalculations()
        {

        }
    }

}
