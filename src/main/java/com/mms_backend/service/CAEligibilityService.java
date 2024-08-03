package com.mms_backend.service;

import com.mms_backend.dto.CAEligibilityDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.CAEligibilityRepo;
import com.mms_backend.entity.CAEligibilityEntity;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CAEligibilityService {
    @Autowired
    private CAEligibilityRepo caEligibilityRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ResponseDTO responseDTO;

    public ResponseDTO getCAEliFfSt(String course_id){
        List<CAEligibilityEntity> caEligibilityEntityList = caEligibilityRepo.findAllCbyCid(course_id);
        if (caEligibilityEntityList.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Courses Not Found");
        }else {
            List<CAEligibilityDTO> caEligibilityDTOList = modelMapper.map(caEligibilityEntityList, new TypeToken<ArrayList<CAEligibilityDTO>>(){}.getType());
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(caEligibilityDTOList);
            responseDTO.setMessage("Courses Found!");
        }
        return responseDTO;
    }
}
