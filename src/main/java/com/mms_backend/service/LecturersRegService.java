package com.mms_backend.service;

import com.mms_backend.dto.LecturersRegDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.dto.UserDTO;
import com.mms_backend.entity.User;
import com.mms_backend.repository.LecturersRegRepo;
import com.mms_backend.entity.LecturersRegEntity;
import com.mms_backend.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LecturersRegService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LecturersRegRepo lecturersRegRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ResponseDTO responseDTO;

    public ResponseDTO saveLecturerDetails(LecturersRegDTO lecturerDetailObj){

        if(lecturerDetailObj==null)
        {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage("Empty");
        }
        else
        {
            LecturersRegEntity lecturersRegEntity = modelMapper.map(lecturerDetailObj ,LecturersRegEntity.class);


            try
            {
                lecturersRegRepo.save(lecturersRegEntity);
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(lecturersRegEntity);
                responseDTO.setMessage("User saved Successfully");
            }catch (Exception e)
            {
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(lecturersRegEntity);
                responseDTO.setMessage("User can not be saved ");
            }
        }
        return responseDTO;
    }


    public ResponseDTO getAllLecturers(String department_id){


        try
        {
            List<User> lecList = userRepo.findAllLecturersOfDepartment(department_id);
            if(!lecList.isEmpty())
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(modelMapper.map(lecList,new TypeToken<ArrayList<UserDTO>>(){}.getType()));
                responseDTO.setMessage("Data found");
            }
            else {
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(null);
                responseDTO.setMessage("No Data found");
            }

        }
        catch (Exception e)
        {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO getAllUsersData(){
        try
        {
            List<User> lecList = userRepo.findStaff();
            if(!lecList.isEmpty())
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(modelMapper.map(lecList,new TypeToken<ArrayList<UserDTO>>(){}.getType()));
                responseDTO.setMessage("Data found");
            }
            else {
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(null);
                responseDTO.setMessage("No Data found");
            }
        }
        catch (Exception e)
        {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO getAllLecturersData(){
        try
        {
            List<User> lecList = userRepo.findAllLecturers();
            if(!lecList.isEmpty())
            {
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(modelMapper.map(lecList,new TypeToken<ArrayList<UserDTO>>(){}.getType()));
                responseDTO.setMessage("Data found");
            }
            else {
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(null);
                responseDTO.setMessage("No Data found");
            }
        }
        catch (Exception e)
        {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage(e.getMessage());
        }
        return responseDTO;
    }

    public ResponseDTO editLecturerDetails(LecturersRegDTO lecturersRegDTO){
        if(lecturersRegRepo.existsById(lecturersRegDTO.getId()))
        {
            try {
                lecturersRegRepo.save(modelMapper.map(lecturersRegDTO,LecturersRegEntity.class));
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(null);
                responseDTO.setMessage("Updated Successfully");
            }catch (Exception e)
            {
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(lecturersRegDTO);
                responseDTO.setMessage("Can not update");
            }

        }
        return responseDTO;

    }

    public ResponseDTO deleteLecByID(int id)
    {
        if(lecturersRegRepo.existsById(id))
        {
            LecturersRegDTO lec=null;
            try
            {
                Optional<LecturersRegEntity> lecturersRegEntity=lecturersRegRepo.findById(id);
                 lec=modelMapper.map(lecturersRegEntity,LecturersRegDTO.class);
                 lecturersRegEntity.get().setIs_deleted(true);
                lecturersRegRepo.save(lecturersRegEntity.get());
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(lec);
                responseDTO.setMessage("Lecturer deleted Successfully");
            }catch (Exception e)
            {
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(lec);
                responseDTO.setMessage("Lecturer can not be deleted ");
            }

        }
        else {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage("Invalid lecturer id");
        }
        return responseDTO;
    }

    public ResponseDTO getAllLecId(){
        ArrayList<String> userIDlist = new ArrayList<>();
        List<LecturersRegEntity> list=lecturersRegRepo.findAll();
        if(!list.isEmpty())
        {
            for (LecturersRegEntity lecturersRegEntity : list) {
                userIDlist.add(lecturersRegEntity.getUser_id());
            }
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(userIDlist);
            responseDTO.setMessage("get all Lecturer IDs");

        }
        else
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setMessage("No data found");
            responseDTO.setContent(null);
        }

        return responseDTO;
    }

    public ResponseDTO insertALLUsersDetailsAsBulk(List<LecturersRegDTO> lecturersRegDTOList) {
        if (lecturersRegDTOList.isEmpty()) {
            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(null);
            responseDTO.setMessage("Empty");
            return responseDTO;
        } else {
            List<LecturersRegEntity> lecturersRegEntityList = modelMapper.map(lecturersRegDTOList, new TypeToken<ArrayList<LecturersRegEntity>>(){}.getType());

            List<LecturersRegEntity> entitiesToSave = new ArrayList<>();
            for (LecturersRegEntity entity : lecturersRegEntityList) {
                if (!lecturersRegRepo.existsById(entity.getId())){
                    entitiesToSave.add(entity);
                    responseDTO.setCode(VarList.RIP_SUCCESS);
                    responseDTO.setContent(null);
                    responseDTO.setMessage("Successfully saved");
                } else {
                    responseDTO.setCode(VarList.RIP_ERROR);
                    responseDTO.setContent(null);
                    responseDTO.setMessage("Duplicate user_id: " + entity.getUser_id());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return responseDTO;

                }
            }

            try {
                lecturersRegRepo.saveAll(entitiesToSave);
                responseDTO.setCode(VarList.RIP_SUCCESS);
                responseDTO.setContent(lecturersRegDTOList);
                responseDTO.setMessage("Details have been uploaded");
            } catch (Exception e) {
                responseDTO.setCode(VarList.RIP_ERROR);
                responseDTO.setContent(null);
                responseDTO.setMessage("Cannot save: " + e.getMessage());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }
        return responseDTO;
    }

}
