package com.mms_backend.service;

import com.mms_backend.dto.AssessmentTypeListDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.AssessmentTypeListRepo;
import com.mms_backend.entity.AssessmentTypeListEntity;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AssessmentTypeListService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AssessmentTypeListRepo assessmentTypeListRepo;

    @Autowired
    ResponseDTO responseDTO;

    public ResponseDTO NewAssessmentType(AssessmentTypeListDTO assessmentTypeListDTO){
        if (assessmentTypeListDTO == null){
            responseDTO.setMessage(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage("Empty");
        }else {
            AssessmentTypeListEntity assessmentTypeListEntity = modelMapper.map(assessmentTypeListDTO, AssessmentTypeListEntity.class);
            try{
                assessmentTypeListRepo.save(assessmentTypeListEntity);
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(assessmentTypeListEntity);
                responseDTO.setMessage("New Assessment Type Saved Successfully");
            }catch (Exception e){
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(assessmentTypeListEntity);
                responseDTO.setMessage("Assessment Type can not be saved ");
            }
        }
        return responseDTO;
    }


    public ResponseDTO allAssessmentTypes(){
        try{
            List<AssessmentTypeListEntity> assessmentTypeListEntityList = assessmentTypeListRepo.findAll();
            if (!assessmentTypeListEntityList.isEmpty()){
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(modelMapper.map(assessmentTypeListEntityList, new TypeToken<ArrayList<AssessmentTypeListDTO>>(){}.getType()));
                responseDTO.setMessage("Data Found");
            }else {
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(null);
                responseDTO.setMessage("No Data found");
            }
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
}
