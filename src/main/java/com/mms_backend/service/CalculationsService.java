package com.mms_backend.service;

import com.mms_backend.dto.CalculationsDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.CalculationsRepo;
import com.mms_backend.entity.Calculations;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CalculationsService
{
    @Autowired
    private CalculationsRepo calculationsRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private ResponseDTO responseDTO;



    public ResponseDTO getMarksCalculations(String course_id)
    {
        List<Calculations> calculations=calculationsRepo.getCalculationresults(course_id);

        if(calculations.isEmpty())
        {
            responseDTO.setMessage("Not Calculated");
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(calculations);
        }
        else
        {
            responseDTO.setMessage("Successfull");
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(calculations,new TypeToken<ArrayList<CalculationsDTO>>(){}.getType()));
        }
        return responseDTO;
    }

    public ResponseDTO getMarksCalculations(String course_id,String student_id)
    {
        List<Calculations> calculations=calculationsRepo.getCalculationresults(course_id,student_id);

        if(calculations.isEmpty())
        {
            responseDTO.setMessage("Not Calculated");
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(calculations);
        }
        else
        {
            responseDTO.setMessage("Successfull");
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(calculations,new TypeToken<ArrayList<CalculationsDTO>>(){}.getType()));
        }
        return responseDTO;

    }
}
