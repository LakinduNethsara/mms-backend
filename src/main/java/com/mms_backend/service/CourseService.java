package com.mms_backend.service;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.CourseDTO;
import com.mms_backend.dto.CourseNameIdDTO;
import com.mms_backend.dto.MarksRangeOfClassDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.entity.CourseEntity;
import com.mms_backend.entity.MarksRangeOfGrade;
import com.mms_backend.repository.CourseRepo;
import com.mms_backend.repository.MarksRangeOfCourseRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    ResponseDTO responseDTO;

    @Autowired
    MarksRangeOfCourseRepo marksRangeOfCourseRepo;


    public List<CourseDTO> findCidCnameByLS(String department_id,int level, int sem) {
        List<CourseEntity> list = courseRepo.findAllcourseOfDeptLS(level,sem,department_id);
        List<CourseDTO> courseDTOList=modelMapper.map(list,new TypeToken<ArrayList<CourseDTO>>(){}.getType());

        return courseDTOList;
    }

    public ResponseDTO findCidCnameByDLS(int level,int sem,String department,String approved_level) {
        try
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            List<Object[]> list = courseRepo.findApprovedCourses(level,sem,department,approved_level);

            if(!list.isEmpty())
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(list);
                responseDTO.setMessage("Successful");
            }
            else
            {
                responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
                responseDTO.setContent(null);
                responseDTO.setMessage("No data found");
            }
        }
        catch (Exception e)
        {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");
        }
        return responseDTO;
    }

    public ResponseDTO getCoursesforLectCertify(String lecturer_id) {
        try
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            List<Object[]> list = courseRepo.findLecturerCertifiedAssignCourse(lecturer_id);

            if(!list.isEmpty())
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(list);
                responseDTO.setMessage("Successful");
            }
            else
            {
                responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
                responseDTO.setContent(null);
                responseDTO.setMessage("No data found");
            }
        }
        catch (Exception e)
        {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");
        }
        return responseDTO;
    }



    public ResponseDTO getAllCourses(){
        List<CourseEntity> courseEntities = courseRepo.findAll();
        if (courseEntities.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Courses not found!");
        }else {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(courseEntities,new TypeToken<ArrayList<CourseDTO>>(){}.getType()));
            responseDTO.setMessage("Courses found!");
        }
        return responseDTO;
    }
    public ResponseDTO insertCoursesAsBulk(List<CourseDTO> courseDTOS){
        List<CourseEntity> coursesAsBulk  = modelMapper.map(courseDTOS,new TypeToken<ArrayList<CourseEntity>>(){}.getType());
        try {
            courseRepo.saveAll(coursesAsBulk);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseDTOS);
            responseDTO.setMessage("Courses have been inserted");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(courseDTOS);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
    public ResponseDTO insertACourse(CourseDTO courseDTO){
        CourseEntity insertOneCourse = modelMapper.map(courseDTO,CourseEntity.class);
        try {
            courseRepo.save(insertOneCourse);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseDTO);
            responseDTO.setMessage("Course has been inserted");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(courseDTO);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
    public ResponseDTO getACourseById(int id){
        if (courseRepo.existsById(id)){
            Optional<CourseEntity> courseById = courseRepo.findById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseById);
            responseDTO.setMessage("Data found");

        }else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Data not found");
        }
        return responseDTO;
    }


    public ResponseDTO getACourseBycourseId(String id){

            CourseEntity courseById = courseRepo.findBycourseId(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseById);
            responseDTO.setMessage("Data found");

        return responseDTO;
    }
    public ResponseDTO updateACourseById(CourseDTO courseDTO){
        CourseEntity updateOneCourseById = modelMapper.map(courseDTO,CourseEntity.class);
        try {
            courseRepo.save(updateOneCourseById);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseDTO);
            responseDTO.setMessage("Course has been updated");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(courseDTO);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
    public ResponseDTO deleteACourseById(int id){
        if (courseRepo.existsById(id)){
            courseRepo.deleteById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(id);
            responseDTO.setMessage("Course has been deleted");
        }else {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(id);
            responseDTO.setMessage("Course id not found");
        }
        return responseDTO;
    }

    public ResponseDTO getAllCIDs(){
        ArrayList<String> allCIDs = new ArrayList<>();
        List<CourseEntity> list=courseRepo.findAll();
        if(!list.isEmpty())
        {
            for (CourseEntity courseEntity : list) {
                allCIDs.add(courseEntity.getCourse_id());
            }
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(allCIDs);
            responseDTO.setMessage("get all Course IDs");

        }
        else
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setMessage("No data found");
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO getApprovedCourse(String email){
        List<Object[]> courseEntities = courseRepo.findLecturerApprovedCourses(email);
        if (courseEntities.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Data Not Found!");
        }else {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseEntities);
            responseDTO.setMessage("Data Found");
        }
        return responseDTO;
    }

    public ResponseDTO getAllRegCourseForCC(String email){
        List<Object[]> courseEntityList = courseRepo.findCCRegCourses(email);
        if (courseEntityList.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Registered Courses Not Found!");
        }else {
//            List<CourseDTO> courseDTOList = modelMapper.map(courseEntityList,new TypeToken<ArrayList<CourseDTO>>(){}.getType());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseEntityList);
            responseDTO.setMessage("Registered Courses Found!");
        }
        return responseDTO;
    }


    public ResponseDTO getGradeMargin(String course_id,String academic_year)
    {
        List<MarksRangeOfClassDTO> courseDTOList=new ArrayList<>();
        List<MarksRangeOfGrade> marksRangeOfGrades=marksRangeOfCourseRepo.getMarksRange(course_id,academic_year);
        if (marksRangeOfGrades.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Not Successfull!");
        }else {
            for (MarksRangeOfGrade ele : marksRangeOfGrades)
            {
                MarksRangeOfClassDTO element = modelMapper.map(ele, MarksRangeOfClassDTO.class);
                courseDTOList.add(element);
            }
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseDTOList);
            responseDTO.setMessage("Successful!");
        }
        return responseDTO;
    }

    public ResponseDTO updateGradeMargin(String course_id,String academic_year,String grade,double margin)
    {

        try
        {
            MarksRangeOfGrade marksRangeOfGrades=marksRangeOfCourseRepo.getMarkRangeofGrade(course_id,academic_year,grade);
            marksRangeOfGrades.setMargin_of_grade(margin);
            marksRangeOfCourseRepo.save(marksRangeOfGrades);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(marksRangeOfGrades);
            responseDTO.setMessage("Successful!");
        }catch (Exception e)
        {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage("Unsuccessful!");
        }
        return responseDTO;
    }



}
