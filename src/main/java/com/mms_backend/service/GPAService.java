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




    public float calculateGPAForCourse(String grade,int credit) {
        float gpaPoints = 0.0f;

        if (grade.equals("A+") || grade.equals("A")) {
            gpaPoints = 4.00f;
        } else if (grade.equals("A-")) {
            gpaPoints = 3.70f;
        } else if (grade.equals("B+")) {
            gpaPoints = 3.30f;
        } else if (grade.equals("B")) {
            gpaPoints = 3.00f;
        } else if (grade.equals("B-")) {
            gpaPoints = 2.70f;
        } else if (grade.equals("C+")) {
            gpaPoints = 2.30f;
        } else if (grade.equals("C")) {
            gpaPoints = 2.00f;
        } else if (grade.equals("C-")) {
            gpaPoints = 1.70f;
        } else if (grade.equals("D+")) {
            gpaPoints = 1.30f;
        } else if (grade.equals("D")) {
            gpaPoints = 1.00f;
        } else if (grade.equals("E") || grade.equals("F")) {
            gpaPoints = 0.00f;
        }

        float gpaForCourse = gpaPoints * credit;

        return gpaForCourse;
    }


    public void gpaCalculate(String course_id,int credit)
    {

    }



}
