package com.mms_backend.controller;


import com.mms_backend.dto.NotificationsDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.service.NotificationsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "false")
@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationsController
{
    @Autowired
    private NotificationsService notificationsService;

    @PostMapping("/sendNotification")
    public ResponseEntity sendNotification(@RequestBody NotificationsDTO notificationsDTO)
    {
       ResponseDTO responseDTO= notificationsService.sendNotifications(notificationsDTO);
        if(responseDTO.getCode().equals(VarList.RIP_SUCCESS))
        {
            return new ResponseEntity(responseDTO, HttpStatus.CREATED);
        }
        else
            return new ResponseEntity(responseDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
