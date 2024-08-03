package com.mms_backend.service;

import com.mms_backend.dto.AttendanceEligibilityDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.AttendanceEligibilityRepo;
import com.mms_backend.entity.AttendanceEligibility;
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
public class AttendanceEligibilityService
{
    @Autowired
    ResponseDTO responseDTO;
    @Autowired
    private AttendanceEligibilityRepo attendanceEligibilityRepo;

    @Autowired
    private ModelMapper modelMapper;

    public AttendanceEligibilityDTO getAttendanceEligibilityByStuIdCourseId(String course_id,String student_id)
    {
        AttendanceEligibility list=attendanceEligibilityRepo.getAttendanceByStuIdCourseId(course_id,student_id);

        if(list==null)
        {
            return  null;
        }
        else

        return modelMapper.map(list, AttendanceEligibilityDTO.class);

    }


    public ResponseDTO getAllAttendance(){
        List<AttendanceEligibility> attendanceEligibilities = attendanceEligibilityRepo.findAll();
        if (attendanceEligibilities.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Attendances not found!");
        }else {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(modelMapper.map(attendanceEligibilities, new TypeToken<ArrayList<AttendanceEligibilityDTO>>(){}.getType()));
            responseDTO.setMessage("Attendances found!");
        }
        return responseDTO;
    }
    public ResponseDTO insertAttendancesAsBulk(List<AttendanceEligibilityDTO> attendanceEligibilityDTOS){
        List<AttendanceEligibility> insertAttendancesAsBulk = modelMapper.map(attendanceEligibilityDTOS,new TypeToken<ArrayList<AttendanceEligibility>>(){}.getType());
        try {
            attendanceEligibilityRepo.saveAll(insertAttendancesAsBulk);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(attendanceEligibilityDTOS);
            responseDTO.setMessage("Attendances have been inserted");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(attendanceEligibilityDTOS);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }
    public ResponseDTO insertAAttendance(AttendanceEligibilityDTO attendanceEligibilityDTO){
        AttendanceEligibility insertOneAttendance = modelMapper.map(attendanceEligibilityDTO,AttendanceEligibility.class);
        try {
            attendanceEligibilityRepo.save(insertOneAttendance);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(attendanceEligibilityDTO);
            responseDTO.setMessage("Attendance has been inserted");
        } catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(attendanceEligibilityDTO);
            responseDTO.setMessage(e.getMessage());

        }
        return responseDTO;
    }
    public ResponseDTO getAAttendanceById(int id){
        if (attendanceEligibilityRepo.existsById(id)){
            Optional<AttendanceEligibility> attendanceById = attendanceEligibilityRepo.findById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(attendanceById);
            responseDTO.setMessage("Data found");

        }else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Data not found");
        }
        return responseDTO;
    }
    public ResponseDTO updateAAttendanceById(AttendanceEligibilityDTO attendanceEligibilityDTO){
        AttendanceEligibility updateOneAttendanceById = modelMapper.map(attendanceEligibilityDTO,AttendanceEligibility.class);
        try {
            attendanceEligibilityRepo.save(updateOneAttendanceById);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(attendanceEligibilityDTO);
            responseDTO.setMessage("Attendance has been updated");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(attendanceEligibilityDTO);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;

    }
    public ResponseDTO deleteAAttendanceById(int id){
        if (attendanceEligibilityRepo.existsById(id)){
            attendanceEligibilityRepo.deleteById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(id);
            responseDTO.setMessage("Attendance has been deleted");
        }else {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(id);
            responseDTO.setMessage("Attendance id not found");
        }
        return responseDTO;
    }




}
