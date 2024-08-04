package com.mms_backend.service;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.CourseDTO;
import com.mms_backend.dto.CourseNameIdDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.entity.CourseEntity;
import com.mms_backend.repository.CourseRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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



    public List<CourseDTO> findCidCnameByLS(String department_id,int level, int sem) {
        List<CourseEntity> list = courseRepo.findAllcourseOfDeptLS(level,sem,department_id);
        List<CourseDTO> courseDTOList=modelMapper.map(list,new TypeToken<ArrayList<CourseDTO>>(){}.getType());

        return courseDTOList;
    }

    public ResponseDTO findCidCnameByDLS(int level,int sem,String department,String approved_level,String academic_year) {
        try
        {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            List<CourseEntity> list = courseRepo.findApprovedCourses(level,sem,department,approved_level,academic_year);
            List<CourseNameIdDTO> courseNameIdDTOs = new ArrayList<>();
            if(!list.isEmpty())
            {
                List<CourseDTO> courseDTOList=modelMapper.map(list,new TypeToken<ArrayList<CourseDTO>>(){}.getType());
                for (CourseDTO courseDTO : courseDTOList) {
                    CourseNameIdDTO courseNameIdDTO = new CourseNameIdDTO();
                    courseNameIdDTO.setCourse_name(courseDTO.getCourse_name());
                    courseNameIdDTO.setCourse_id(courseDTO.getCourse_id());
                    courseNameIdDTOs.add(courseNameIdDTO);
                }
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(courseNameIdDTOs);
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
            List<CourseEntity> list = courseRepo.findLecturerCertifiedAssignCourse(lecturer_id);
            List<CourseNameIdDTO> courseNameIdDTOs = new ArrayList<>();
            if(!list.isEmpty())
            {
                List<CourseDTO> courseDTOList=modelMapper.map(list,new TypeToken<ArrayList<CourseDTO>>(){}.getType());

                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(courseDTOList);
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
        List<CourseEntity> courseEntities = courseRepo.findLecturerApprovedCourses(email);
        if (courseEntities.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Data Not Found!");
        }else {
            List<CourseDTO> list=modelMapper.map(courseEntities,new TypeToken<ArrayList<CourseDTO>>(){}.getType());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(list);
            responseDTO.setMessage("Data Found");
        }
        return responseDTO;
    }

    public ResponseDTO getAllRegCourseForCC(String email){
        List<CourseEntity> courseEntityList = courseRepo.findCCRegCourses(email);
        if (courseEntityList.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Registered Courses Not Found!");
        }else {
            List<CourseDTO> courseDTOList = modelMapper.map(courseEntityList,new TypeToken<ArrayList<CourseDTO>>(){}.getType());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseDTOList);
            responseDTO.setMessage("Registered Courses Found!");
        }
        return responseDTO;
    }



}
