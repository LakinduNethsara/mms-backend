package com.mms_backend.service;


import com.mms_backend.dto.GPADTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.GPARepo;
import com.mms_backend.entity.GPA;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GPAService
{
    @Autowired
    private GPARepo gpaRepo;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    ModelMapper mp;

    public ResponseDTO getGPAByLevelSemester(String level,String semester)
    {
        List<GPA> list=gpaRepo.findGPAByLevelSemester(level,semester);

        if(!list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(mp.map(list,new TypeToken<ArrayList<GPADTO>>(){}.getType()));

            responseDTO.setMessage("Data found");

        }
        else
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No Data found");
        }
        return responseDTO;
    }

    public ResponseDTO getGPAByStID(String student_id)
    {

        GPA gpa=gpaRepo.findGPAByStId(student_id);

        if(gpa!=null)
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(mp.map(gpa,new TypeToken<ArrayList<GPADTO>>(){}.getType()));

            responseDTO.setMessage("Data found");

        }
        else
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No Data found");
        }
        return responseDTO;
    }

}
