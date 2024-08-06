package com.mms_backend.service;

import com.mms_backend.dto.EvaluationCriteriaDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.EvaluationCriteriaRepo;
import com.mms_backend.entity.EvaluationCriteria;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EvaluationCriteriaService
{

    @Autowired
    private EvaluationCriteriaRepo evaluationCriteriaRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;



    public ResponseDTO getEvaluationCriteria(String course_id)
    {
        List<EvaluationCriteria> list=evaluationCriteriaRepo.getEvaluationCriteria(course_id);
        if(list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");

            return responseDTO;
        }

        else
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(list,new TypeToken<ArrayList<EvaluationCriteriaDTO>>(){}.getType()));
            responseDTO.setMessage("Successfull");
        }
       return  responseDTO;
    }

    public ResponseDTO insertEvCriteria(List<EvaluationCriteriaDTO> evaluationCriteriaDTOList){

        List<EvaluationCriteria> EvCriteriaInsert = modelMapper.map(evaluationCriteriaDTOList, new TypeToken<ArrayList<EvaluationCriteria>>(){}.getType());
        try{
            evaluationCriteriaRepo.saveAll(EvCriteriaInsert);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(evaluationCriteriaDTOList);
            responseDTO.setMessage("Data have been inserted!");

        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(evaluationCriteriaDTOList);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO getCA(String course_id)
    {
        List<EvaluationCriteria> list=evaluationCriteriaRepo.getCA(course_id);
        if(list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");

            return responseDTO;
        }

        else
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(list,new TypeToken<ArrayList<EvaluationCriteriaDTO>>(){}.getType()));
            responseDTO.setMessage("Successfull");
        }
       return  responseDTO;
    }
}
