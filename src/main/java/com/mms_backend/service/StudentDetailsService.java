package com.mms_backend.service;

import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.dto.StudentDetailsDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.dto.UserDTO;
import com.mms_backend.entity.User;
import com.mms_backend.repository.StudentDetailsRepo;
import com.mms_backend.entity.StudentDetailsEntity;
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
public class StudentDetailsService {
    @Autowired
    private StudentDetailsRepo studentDetailsRepo;

    @Autowired
    private ModelMapper mmp;

    @Autowired
    ResponseDTO responseDTO;

    public ResponseDTO getAllStudentsD(){
        List<User> courseEntities = studentDetailsRepo.findAllStudents();
        if (courseEntities.isEmpty()){
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Students Details not found!");
        }else {
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(mmp.map(courseEntities, new TypeToken<ArrayList<UserDTO>>(){}.getType()));
            responseDTO.setMessage("Students Details found!");
        }
        return responseDTO;
    }
    public ResponseDTO insertStudentDetailsAsBulk(List <UserDTO> studentDetailsDTOS){

        if(studentDetailsDTOS.isEmpty())
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(studentDetailsDTOS);
            responseDTO.setMessage("Empty Data");
        }
        else {
            List<User> studentDetailsAsBulk = mmp.map(studentDetailsDTOS,new TypeToken<ArrayList <User>>(){}.getType());
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            try {
                studentDetailsRepo.saveAll(studentDetailsAsBulk);
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(studentDetailsDTOS);
                responseDTO.setMessage("Students Details have been uploaded");
            }catch (Exception e){
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(studentDetailsDTOS);
                responseDTO.setMessage(e.getMessage());
            }
        }


        return responseDTO;
    }

    public ResponseDTO insertAStudentD(UserDTO studentDetailsDTO){
        User insertOneStudentD = mmp.map(studentDetailsDTO,User.class);
        try {
            studentDetailsRepo.save(insertOneStudentD);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(studentDetailsDTO);
            responseDTO.setMessage("A Student Details has been inserted");
        }catch (Exception e){
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(studentDetailsDTO);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO getAStudentDById(int id){
        if (studentDetailsRepo.existsById(id)){

            Optional<User> StudentDById = studentDetailsRepo.findById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(StudentDById);
            responseDTO.setMessage("Data found");

        }else {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(null);
            responseDTO.setMessage("Data not found");
        }

        return responseDTO;
    }

    public ResponseDTO updateAStudentDById(UserDTO studentDetailsDTO){
        if (studentDetailsRepo.existsById(studentDetailsDTO.getId())){
            try {
                studentDetailsRepo.save(mmp.map(studentDetailsDTO,User.class));
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(studentDetailsDTO);
                responseDTO.setMessage("The Student Details has been updated");
            }catch (Exception e){
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(studentDetailsDTO);
                responseDTO.setMessage("can not update");
            }
        }


        return responseDTO;

    }

    public ResponseDTO deleteAStudentDById(int id){
        if (studentDetailsRepo.existsById(id)){
            studentDetailsRepo.deleteById(id);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(id);
            responseDTO.setMessage("The Student Details has been deleted");
        }else {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(id);
            responseDTO.setMessage("The Student Details id not found");
        }
        return responseDTO;
    }

}
