package com.mms_backend.service;

import com.mms_backend.dto.EvaluationCriteriaNameDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.EvaluationCriteriaNameRepo;
import com.mms_backend.entity.EvaluationCriteriaNameEntity;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EvaluationCriteriaNameService {
    @Autowired
    private EvaluationCriteriaNameRepo evaluationCriteriaNameRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;


    public ResponseDTO insertEvCriteriaName(List<EvaluationCriteriaNameDTO> evaluationCriteriaNameDTOList){
        List<EvaluationCriteriaNameEntity> EvCriteriaNameInsert = modelMapper.map(evaluationCriteriaNameDTOList, new TypeToken<ArrayList<EvaluationCriteriaNameEntity>>(){}.getType());
        try {
            evaluationCriteriaNameRepo.saveAll(EvCriteriaNameInsert);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(evaluationCriteriaNameDTOList);
            responseDTO.setMessage("Data have been inserted!");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(evaluationCriteriaNameDTOList);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO getAssessmentTypes(String course_id,String type)
    {
        List<EvaluationCriteriaNameEntity> list=evaluationCriteriaNameRepo.getAssessmentTypes(course_id,type);
        try {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(list);
            responseDTO.setMessage("Successful");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

}
