package com.mms_backend.service;

import com.mms_backend.dto.MarksDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.entity.*;
import com.mms_backend.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarksService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MarksRepo marksRepo;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private StudentRegCoursesRepo studentRegCoursesRepo;

    @Autowired
    private EvaluationCriteriaRepo evaluationCriteriaRepo;

    @Autowired
    private CalculationsRepo calculationsRepo;

    @Autowired
    private FinalSelectionRepo finalSelectionRepo;

//    @Autowired
//    private Calculations calculationsObj;



    public List<MarksDTO> getAllScore(){

        List<MarksEntity> markList=marksRepo.findAll();
        return modelMapper.map(markList,new TypeToken<ArrayList<MarksDTO>>(){}.getType());
    }


    public List<MarksDTO> getAllScoreByCourseId(String course_id,String academic_year){

        List<MarksEntity> list=marksRepo.findStudentMarksByCourseID(course_id,academic_year);

        return modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType());
    }

    public void editScore(MarksDTO marksDTO)
    {
        marksRepo.save(modelMapper.map(marksDTO,MarksEntity.class));
    }

    public Optional<MarksEntity> getScoreByID(int id)
    {
        return marksRepo.findById(id);
    }



    public ResponseDTO getScoreByStudent_ID(String student_id)
    {
        List<MarksEntity> list=marksRepo.getScoreByStudent_ID(student_id);


        if(list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Not found");
        }
        else
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType()));
            responseDTO.setMessage("Not found");
        }
        return responseDTO;
    }


    public List<MarksDTO> getScoreByStuIDCourseID(String course_id,String student_id)
    {
        List<MarksEntity> list=marksRepo.getScoreByStuIDCourseID(course_id,student_id);

        if(list.isEmpty())
        {
            return null;
        }
        else
        return modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType());
    }

    public List<MarksDTO> getScoreByLS(String level,String  semester)
    {
        List<MarksEntity> list=marksRepo.getScoreByLS(level,semester);

        if(list.isEmpty())
        {
            return null;
        }
        else
            return modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType());

    }

    public ResponseDTO getenteredCAMarks(String course_id,String academic_year)
    {
        List<MarksEntity> list=marksRepo.getCAMarks(course_id,academic_year);
        if(list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");
        }
        else{
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType()));
            responseDTO.setMessage("Data found");
        }
            return responseDTO;
    }

    public ResponseDTO getenteredFAMarks(String course_id,String academic_year)
    {
        List<MarksEntity> list=marksRepo.getFAMarks(course_id, academic_year);
        if(list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");
        }
        else{
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType()));
            responseDTO.setMessage("Data found");
        }
        return responseDTO;
    }

    public ResponseDTO saveCAMarks(List<MarksDTO> list)
    {
         List<Calculations> marksCalculationsList=new ArrayList<>(); //create list to all students marks calculations

        try {
            List<MarksEntity> marks=modelMapper.map(list,new TypeToken<ArrayList<MarksEntity>>(){}.getType());

            marksRepo.saveAll(marks); //save all marks
            String course_id = "";
            String M_evaluationCriteriaID = "";

            for (MarksEntity mark : marks) {
                String cid = mark.getCourse_id();
                course_id = cid; //get course id

                String MeID = mark.getEvaluation_criteria_id();
                M_evaluationCriteriaID = MeID; //get evaluation criteria id
            }

            EvaluationCriteria getDetailsByEvaluationCriteriaID = evaluationCriteriaRepo.getEvaluationCriteriaByEvaluationCriteria_id(M_evaluationCriteriaID); //getEvaluationCriteriaDetails from EvaluationCriteriaRepo by EvaluationCriteriaID
            System.out.println("hello-------------"+getDetailsByEvaluationCriteriaID);

            int numOfConducted = getDetailsByEvaluationCriteriaID.getNo_of_conducted();
            int numOfTaken = getDetailsByEvaluationCriteriaID.getNo_of_taken();
            int percentage = getDetailsByEvaluationCriteriaID.getPercentage();

            int noOfConductedAssessmentsByEvaluationCriteriaID = marksRepo.getConductedAssessmentsByEvaluationCriteriaID(M_evaluationCriteriaID);
            System.out.println("hey ------------------- : "+noOfConductedAssessmentsByEvaluationCriteriaID);


            if(noOfConductedAssessmentsByEvaluationCriteriaID == numOfConducted){
                List<MarksEntity> marksDetailsByEvaluationCriteriaStudentAcademicYear; //create list to get marks by student id, academic year and evaluation criteria id

                List<Calculations> calculatedMarksList = new ArrayList<>();

                //set course id and evaluation criteria id to the calculations object

                //loop for all students
                for(MarksEntity mark : marks){

                    Calculations calculationsObj = new Calculations(); //create object from calculation entity
                    calculationsObj.setCourse_id(course_id);
                    calculationsObj.setEvaluation_criteria_id(M_evaluationCriteriaID);
//                    System.out.println("student id by pass for loop : "+mark.getStudent_id());

                    marksDetailsByEvaluationCriteriaStudentAcademicYear = marksRepo.getMarksByStudentID(mark.getStudent_id(),mark.getAcademic_year(),M_evaluationCriteriaID);
                    calculationsObj.setAcademic_year(mark.getAcademic_year()); //set academic year to the calculations object
                    //add marks in to a list
                    List<Double> marksArray = new ArrayList<>();
                    for (MarksEntity toMaxMarks : marksDetailsByEvaluationCriteriaStudentAcademicYear){
                        marksArray.add(Double.valueOf(toMaxMarks.getAssignment_score()));
                    }
                    // sorting descending
                    marksArray.sort(Collections.reverseOrder());
                    List<Double> maxMarksList = marksArray.subList(0,numOfTaken);
                    double AVGOfMaxMarks = (maxMarksList.stream().mapToDouble(Double::doubleValue).sum())/numOfTaken;
                    double AVG_Marks_percentage = AVGOfMaxMarks * percentage / 100;

                    //add calculated marks to the calculations object
                    calculationsObj.setMark(String.valueOf(AVGOfMaxMarks));
                    calculationsObj.setPercentage(String.valueOf(AVG_Marks_percentage));
                    calculationsObj.setStudent_id(mark.getStudent_id());

                    calculationsRepo.save(calculationsObj); //save calculated marks
                    System.out.println("calculatedMarksList : "+calculationsObj);

                }
            }

            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(null);
            responseDTO.setMessage("Successfully");

        }catch (Exception e)
        {
            responseDTO.setCode(VarList.RIP_FAIL);
            responseDTO.setContent(null);
            responseDTO.setMessage("Error saving Marks");
        }
        return responseDTO;
    }

    public List<Object> getMarksForCA(String course_id, String academic_year)
    {
        List<Object> list=marksRepo.getMarksForCA(course_id,academic_year);

        return list;
    }




    //------------------------------------------------Select 1st , 2nd or average of end theory marks and insert
    public void GenerateFinalMarksFromEnd( String assignment_name,  String course_id,  String selected,  String academic_year){
        List<StudentRegCourses> studentList = studentRegCoursesRepo.getAllStudentsByCID(course_id,academic_year);

//        FinalMarksselection finalMarksselection = new FinalMarksselection();
//
//        finalMarksselection.setCourseId(course_id);
//        finalMarksselection.setSelected(selected);
//        finalMarksselection.setAssessmentType(assignment_name);
//        finalMarksselection.setAcademicYear(academic_year);
//
//        try{
//            finalSelectionRepo.save(finalMarksselection);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }

        for (StudentRegCourses student : studentList) {

            System.out.println(student.getStudent_id());  // Example: print student ID
            marksRepo.GenerateFinalMarksFromEnd(student.getStudent_id(),assignment_name,course_id,selected,academic_year);


        }




    }




}
