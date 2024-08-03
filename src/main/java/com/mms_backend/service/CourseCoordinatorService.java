package com.mms_backend.service;

import com.mms_backend.dto.CourseCoordinatorDTO;
import com.mms_backend.dto.LecturerCoursRegDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.CourseCoordinatorRepo;
import com.mms_backend.entity.CourseCoordinatorEntity;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseCoordinatorService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ResponseDTO responseDTO;
    @Autowired
    CourseCoordinatorRepo courseCoordinatorRepo;
    @Autowired
    LecturerCourseRegService lecturerCourseRegService;

    public ResponseDTO getAllCCs(){
        List<CourseCoordinatorEntity> courseCoordinatorEntities = courseCoordinatorRepo.findAll();
        if (courseCoordinatorEntities.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Course Coordinator not found!");
        }else {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(courseCoordinatorEntities, new TypeToken<ArrayList<CourseCoordinatorDTO>>(){}.getType()));
            responseDTO.setMessage("Course Coordinator found!");
        }
        return responseDTO;
    }

    public ResponseDTO insertCC(CourseCoordinatorDTO courseCoordinatorDTO){
        CourseCoordinatorEntity insertOneCC = modelMapper.map(courseCoordinatorDTO,CourseCoordinatorEntity.class);
        try {
            courseCoordinatorRepo.save(insertOneCC);
            System.out.println(courseCoordinatorDTO.getSelectedLecturerIds());
            for (String lecturer_id : courseCoordinatorDTO.getSelectedLecturerIds()){
                    List<LecturerCoursRegDTO> list = new ArrayList<>();
                    LecturerCoursRegDTO lecturerCoursRegDTO = new LecturerCoursRegDTO();
                    lecturerCoursRegDTO.setCourse_id(courseCoordinatorDTO.getCourse_id());
                    lecturerCoursRegDTO.setUser_id(lecturer_id);
                    list.add(lecturerCoursRegDTO);
                    lecturerCourseRegService.addLecturersToCourse(list);
                System.out.println(lecturer_id);

            }

            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseCoordinatorDTO);
            responseDTO.setMessage("Course Coordinator has been inserted");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(courseCoordinatorDTO);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO getCCById(int id){
        if (courseCoordinatorRepo.existsById(id)){

            Optional<CourseCoordinatorEntity> CCById = courseCoordinatorRepo.findById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(CCById,CourseCoordinatorDTO.class));
            responseDTO.setMessage("Data found");

        }else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Data not found");
        }

        return responseDTO;
    }

    public ResponseDTO updateCCById(CourseCoordinatorDTO courseCoordinatorDTO){
        CourseCoordinatorEntity updateOneCCById = modelMapper.map(courseCoordinatorDTO,CourseCoordinatorEntity.class);
        try {
            courseCoordinatorRepo.save(updateOneCCById);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseCoordinatorDTO);
            responseDTO.setMessage("Course Coordinator has been updated");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(courseCoordinatorDTO);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO deleteCCById(int id){
        if (courseCoordinatorRepo.existsById(id)){
            courseCoordinatorRepo.deleteById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(id);
            responseDTO.setMessage("Course Coordinator has been deleted");
        }else {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(id);
            responseDTO.setMessage("Course Coordinator id not found");
        }
        return responseDTO;
    }


    public ResponseDTO getCCbyCourse(String course_id)
    {
        CourseCoordinatorEntity courseCoordinatorEntity=courseCoordinatorRepo.getCCBycourse(course_id);
        if(courseCoordinatorEntity!=null)
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseCoordinatorEntity.getUser_id());
            responseDTO.setMessage("Successfully get Course coordinator");
        }else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("error getting Course coordinator");
        }
        return responseDTO;
    }

    public ResponseDTO getAllCIDForCC(String user_name){
        List<CourseCoordinatorEntity> courseCoordinatorEntities = courseCoordinatorRepo.getAllCidToCourseCriteria(user_name);
        if (courseCoordinatorEntities.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Course_ids Not Found!");
        }else {
            List<CourseCoordinatorDTO> courseCoordinatorDTOList = modelMapper.map(courseCoordinatorEntities, new TypeToken<ArrayList<CourseCoordinatorDTO>>(){}.getType());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(courseCoordinatorDTOList);
            responseDTO.setMessage("Course_ids Found!");
        }
        return responseDTO;
    }


}
