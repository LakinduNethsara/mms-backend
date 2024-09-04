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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            String course_id = "";
            String M_evaluationCriteriaID = "";

            for (MarksEntity mark : marks) {
                String cid = mark.getCourse_id();
                course_id = cid; //get course id

                String MeID = mark.getEvaluation_criteria_id();
                M_evaluationCriteriaID = MeID; //get evaluation criteria id

            }

            List<EvaluationCriteria> getDetails = evaluationCriteriaRepo.getNotETEDetails(course_id);
            String evaluationCriteriaID = "";
            int percentage = 0;

            for (EvaluationCriteria item : getDetails) {
                String evID = item.getEvaluationcriteria_id();
                evaluationCriteriaID = evID;

                int per = item.getPercentage();
                percentage = per;
            }

            if (M_evaluationCriteriaID.equals(evaluationCriteriaID)){
                for (MarksEntity mark : marks) {
                    double score = 0;
                    double avg = 0;
                    if (mark.getAssignment_score() == "AB") {
                        score = 0;
                        avg = 0;
                    }else {
                        score = Double.parseDouble((mark.getAssignment_score()));
                        avg = (score * percentage) / 100;
                    }


                    Calculations singleStudentMarks = new Calculations(); //create object from calculation entity

                    singleStudentMarks.setEvaluation_criteria_id(evaluationCriteriaID);
                    singleStudentMarks.setStudent_id(mark.getStudent_id());
                    singleStudentMarks.setCourse_id(course_id);
                    singleStudentMarks.setMark(String.valueOf(score));
                    singleStudentMarks.setPercentage(String.valueOf(avg));
                    singleStudentMarks.setAcademic_year(mark.getAcademic_year());

                    marksCalculationsList.add(singleStudentMarks);
                    System.out.println("singleStudentMarks " + singleStudentMarks);

                }
                calculationsRepo.saveAll(marksCalculationsList);

                System.out.println("Marks Calculations " + marksCalculationsList);

            }

            marksRepo.saveAll(marks);

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
