package com.mms_backend.service;

import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.dto.StudentMarksDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.StudentMarksRepo;
import com.mms_backend.entity.StudentMarks;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StudentMarksService
{
    @Autowired
    private StudentMarksRepo studentMarksRepo;

    @Autowired
    private ModelMapper mp;

    @Autowired
    private ResponseDTO responseDTO;

 public ResponseDTO  findStudentMarksByLevelSem(String level, String sem)
    {

        List<StudentMarks> markList=studentMarksRepo.findStudentMarksByLevelSemester(level,sem);
        if(!markList.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(mp.map(markList,new TypeToken<ArrayList<StudentMarksDTO>>(){}.getType()));
            responseDTO.setMessage("Student marks found");
        }
        else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Student marks  not found");
        }

        return responseDTO;
    }

    public ResponseDTO  findApprovedStudentMarksByLevelSem(String level, String sem,String approved_level,String department_id,int repeat)
    {

        List<StudentMarks> markList=studentMarksRepo.findStudentMarksByLS(level,sem,approved_level,department_id,repeat);
        if(!markList.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(mp.map(markList,new TypeToken<ArrayList<StudentMarksDTO>>(){}.getType()));
            responseDTO.setMessage("Student marks found");
        }
        else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Student marks  not found");
        }

        return responseDTO;
    }

    public ResponseDTO getMarksforCourseById(String id) {


            List<StudentMarks> list=studentMarksRepo.findCoursecodeOverallScoreByStId(id);
            if(!list.isEmpty())
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(mp.map( list,new TypeToken<ArrayList<StudentMarksDTO>>(){}.getType()));
                responseDTO.setMessage("Data found");
            }
            else
            {
                responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
                responseDTO.setContent(null);
                responseDTO.setMessage("No data found");
            }
            return responseDTO;
    }

//    public ResponseDTO getMarksbyC(String course_id)
//    {
//        List<StudentMarks> list=studentMarksRepo.findMarksByCourse(course_id);
//
//        if(!list.isEmpty())
//        {
//            responseDTO.setCode(VarList.RIP_SUCCESS);
//            responseDTO.setContent(mp.map(list,new TypeToken<ArrayList<StudentMarksDTO>>(){}.getType()));
//            responseDTO.setMessage("Data found");
//        }
//        else
//        {
//            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
//            responseDTO.setContent(null);
//            responseDTO.setMessage("No Data found");
//        }
//        return responseDTO;
//    }

    public ResponseDTO getMarksbySC(String course_id,String student_id)
    {
        StudentMarks studentMarks=studentMarksRepo.findMarksByCS(course_id,student_id);

        if(studentMarks==null)
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("No data found");
        }
        else
        {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(mp.map(studentMarks,StudentMarksDTO.class));
            responseDTO.setMessage("Data found");
        }
            return responseDTO;
    }


    public ResponseDTO saveCAMarks(List<StudentMarksDTO> studentMarksDTO)
    {
        try
        {
            for(StudentMarksDTO student:studentMarksDTO)
            {
                StudentMarks studentMarks=studentMarksRepo.findMarksByCS(student.getCourse_id(),student.getStudent_id());
                if(studentMarks!=null)
                {
                    studentMarks.setTotal_ca_mark(student.getTotal_ca_mark());
                    studentMarks.setCa_eligibility(student.getCa_eligibility());
                    studentMarksRepo.save(studentMarks);
                }
                else {
                    studentMarksRepo.save(mp.map(student,StudentMarks.class));
                }
            }

            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(null);
            responseDTO.setMessage("Successfull");

        }catch (Exception e)
        {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage("Successfull");
        }
        return responseDTO;
    }



}
