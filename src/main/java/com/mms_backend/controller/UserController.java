package com.mms_backend.controller;

import com.mms_backend.Util.VarList;
import com.mms_backend.dto.LoginDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity user_login(@RequestBody LoginDTO loginDTO)
    {
        ResponseDTO responseDTO=userService.UserAuthentication(loginDTO);
        if(responseDTO.getCode()== VarList.RIP_NO_DATA_FOUND)
        {
            return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        }else if(responseDTO.getCode()== VarList.RIP_FAIL)
        {
            return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        } else if (responseDTO.getCode()==VarList.RIP_SUCCESS) {
            return new ResponseEntity(responseDTO, HttpStatus.OK);
        }
        else
            return new ResponseEntity(responseDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
