package com.mms_backend.service;

import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.dto.StudentRegCoursesDTO;
import com.mms_backend.dto.StudentRegisteredCourses;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.StudentRegCoursesRepo;
import com.mms_backend.entity.StudentRegCourses;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StudentRegCoursesServices
{
    @Autowired
    private StudentRegCoursesRepo studentRegCoursesRepo;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseDTO getStudentsByCourseCode(String course_id)
    {
        List<StudentRegCourses> list=studentRegCoursesRepo.getStudentsbyCourseCode(course_id);

        ResponseDTO responseDTO=new ResponseDTO();

        List<StudentRegCoursesDTO> list1=modelMapper.map(list,new TypeToken<ArrayList<StudentRegCoursesDTO>>(){}.getType());

        if(list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");
        }
        else
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(list1);
            responseDTO.setMessage("Successfull");


        }
        return responseDTO;
    }

    public ResponseDTO getStudents()
    {
        List<StudentRegCourses> list=studentRegCoursesRepo.findAll();

        ResponseDTO responseDTO=new ResponseDTO();

        List<StudentRegCoursesDTO> list1=modelMapper.map(list,new TypeToken<ArrayList<StudentRegCoursesDTO>>(){}.getType());

        if(list.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");
        }
        else
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(list1);
            responseDTO.setMessage("Successfull");
        }
        return responseDTO;
    }

    public ResponseDTO insertAllStudents(List<StudentRegisteredCourses> list)
    {
        ResponseDTO responseDTO=new ResponseDTO();
        List<StudentRegCourses> list1=modelMapper.map(list,new TypeToken<ArrayList<StudentRegCourses>>(){}.getType());
        studentRegCoursesRepo.saveAll(list1);

        responseDTO.setCode(VarList.RIP_SUCCESS);
        responseDTO.setContent(list1);
        responseDTO.setMessage("Successfull");
        return responseDTO;
    }
}
