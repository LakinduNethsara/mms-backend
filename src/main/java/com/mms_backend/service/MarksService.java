package com.mms_backend.service;

import com.mms_backend.dto.MarksDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.MarksRepo;
import com.mms_backend.entity.MarksEntity;
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
public class MarksService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MarksRepo marksRepo;

    @Autowired
    private ResponseDTO responseDTO;
    public List<MarksDTO> getAllScore(){

        List<MarksEntity> markList=marksRepo.findAll();
        return modelMapper.map(markList,new TypeToken<ArrayList<MarksDTO>>(){}.getType());
    }


    public List<MarksDTO> getAllScoreByCourseId(String course_id){

        List<MarksEntity> list=marksRepo.findStudentMarksByCourseID(course_id);

        return modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType());
    }

    public void editScore(MarksDTO marksDTO)
    {
        marksRepo.save(modelMapper.map(marksDTO,MarksEntity.class));
    }

    public Optional<MarksEntity> getScoreByID(int id)
    {
        return marksRepo.findById(id);
    }



    public ResponseDTO getScoreByStudent_ID(String student_id)
    {
        List<MarksEntity> list=marksRepo.getScoreByStudent_ID(student_id);


        if(list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Not found");
        }
        else
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType()));
            responseDTO.setMessage("Not found");
        }
        return responseDTO;
    }


    public List<MarksDTO> getScoreByStuIDCourseID(String course_id,String student_id)
    {
        List<MarksEntity> list=marksRepo.getScoreByStuIDCourseID(course_id,student_id);

        if(list.isEmpty())
        {
            return null;
        }
        else
        return modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType());
    }

    public List<MarksDTO> getScoreByLS(String level,String  semester)
    {
        List<MarksEntity> list=marksRepo.getScoreByLS(level,semester);

        if(list.isEmpty())
        {
            return null;
        }
        else
            return modelMapper.map(list,new TypeToken<ArrayList<MarksDTO>>(){}.getType());

    }




}
