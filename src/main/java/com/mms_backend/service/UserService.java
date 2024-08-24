package com.mms_backend.service;

import com.mms_backend.dto.LoginDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.dto.UserDetails;
import com.mms_backend.entity.User;
import com.mms_backend.repository.UserRepo;
import com.mms_backend.Util.VarList;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO UserAuthentication(LoginDTO loginDTO)
    {
        System.out.println(loginDTO.getEmail());
        User user=userRepo.findByUserName(loginDTO.getEmail());
        UserDetails userDetails=new UserDetails();
        userDetails.setUser_id(user.getUser_id());
        userDetails.setFull_name(user.getFull_name());
        userDetails.setEmail(user.getEmail());
        userDetails.setRole(user.getRole());
        userDetails.setIs_deleted(user.getIs_deleted());
        userDetails.setDepartment_id(user.getDepartment_id());
        userDetails.setRegistered_year(user.getRegistered_year());
        userDetails.setName_with_initials(user.getName_with_initials());
        if(user==null)
        {
            responseDTO.setCode(VarList.RIP_NO_DATA_FOUND);
            responseDTO.setContent(loginDTO);
            responseDTO.setMessage("Invalid User Name");
        }else if(!user.getPassword().equals(loginDTO.getPassword()) )
        {
            System.out.println(user);
            responseDTO.setCode(VarList.RIP_FAIL);
            responseDTO.setContent(loginDTO);
            responseDTO.setMessage("Invalid Password");
        }else if(user.getPassword().equalsIgnoreCase(loginDTO.getPassword())){

            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(userDetails);
            responseDTO.setMessage("Successfull");
        }
        return responseDTO;

    }
}
