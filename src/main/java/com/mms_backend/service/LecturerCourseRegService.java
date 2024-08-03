package com.mms_backend.service;

import com.mms_backend.dto.LecturerCoursRegDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.LecturerCourseRegRepo;
import com.mms_backend.entity.LecturerCourseReg;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LecturerCourseRegService {

    @Autowired
    private LecturerCourseRegRepo lecturerCourseRegRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;


    public ResponseDTO addLecturersToCourse(List<LecturerCoursRegDTO> list) {
        if (list.isEmpty()) {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(list);
            responseDTO.setMessage("Empty");
        } else {

            lecturerCourseRegRepo.saveAll(modelMapper.map(list, new TypeToken<ArrayList<LecturerCourseReg>>() {
            }.getType()));
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(list);
            responseDTO.setMessage("Successfully Added");
        }


        return responseDTO;
    }


    public ResponseDTO getLecByCid(String course_id){
        List<LecturerCourseReg> lecturerCourseRegList = lecturerCourseRegRepo.getLecturersRegReposBy(course_id);
        if (!lecturerCourseRegList.isEmpty()){
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setMessage("Data Found");
            responseDTO.setContent(modelMapper.map(lecturerCourseRegList,new TypeToken<ArrayList<LecturerCoursRegDTO>>(){}.getType()));

        }else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No Data Found ");
        }
        return responseDTO;
    }



}
