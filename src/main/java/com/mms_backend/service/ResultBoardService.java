package com.mms_backend.service;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.AR.ResultBoardDTO;
import com.mms_backend.dto.AR.ResultBoardMemberDTO;
import com.mms_backend.dto.GPADTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.entity.AR.ResultBoard;
import com.mms_backend.entity.AR.ResultBoardMember;
import com.mms_backend.repository.AR.ARResultBoardMemberRepo;
import com.mms_backend.repository.AR.ARResultBoardRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ResultBoardService {

    @Autowired
    private ARResultBoardRepo arResultBoardRepo;

    @Autowired
    private ARResultBoardMemberRepo arResultBoardMemberRepo;

    @Autowired
    private ResponseDTO responseDTO;

    @Autowired
    private ModelMapper modelMapper;


    public  ResponseDTO getAvailableResultBoardforHODandCC(String department_id)
    {
        List<ResultBoard> list=arResultBoardRepo.getAvailableResultsBoardsofDepartment(department_id);
        if(list!=null)
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(list,new TypeToken<ArrayList<ResultBoardDTO>>(){}.getType()));

            responseDTO.setMessage("Data found");

        }
        else
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No Results Boards");
        }
        return responseDTO;
    }

    public ResponseDTO getAssignedResultSheetsforDeanCC(String user_id)
    {
        List<ResultBoardMember> list=arResultBoardMemberRepo.getAssignedResultBoardforCC(user_id);
        if(list!=null)
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(list,new TypeToken<ArrayList<ResultBoardMemberDTO>>(){}.getType()));

            responseDTO.setMessage("Data found");

        }
        else
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No Results Boards");
        }
        return responseDTO;
    }
}
