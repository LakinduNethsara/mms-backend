package com.mms_backend.service;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.CourseRelatedDeptDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.entity.CourseRelatedDeptEntity;
import com.mms_backend.repository.CourseRelatedDeptRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CourseRelatedDeptService {
    @Autowired
    private CourseRelatedDeptRepo courseRelatedDeptRepo;
    @Autowired
    private ResponseDTO responseDTO;
    @Autowired
    private ModelMapper modelMapper;

    public ResponseDTO insertACourseToCRDept(List<CourseRelatedDeptDTO> courseRelatedDeptDTO) {
        List<CourseRelatedDeptEntity> insertOneCourse = modelMapper.map(courseRelatedDeptDTO,new TypeToken<ArrayList<CourseRelatedDeptEntity>>(){}.getType());
        try{
            courseRelatedDeptRepo.saveAll(insertOneCourse);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setMessage("Course Inserted Successfully");
            responseDTO.setContent(courseRelatedDeptDTO);

        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setMessage("Course Insertion Failed");
            responseDTO.setContent(e.getMessage());
        }
        return responseDTO;
    }
}

