package com.mms_backend.service;

import com.mms_backend.dto.NotificationsDTO;
import com.mms_backend.dto.ResponseDTO;
import com.mms_backend.Util.VarList;
import com.mms_backend.repository.NotificationsRepo;
import com.mms_backend.entity.NotificationsEntity;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Transactional
public class NotificationsService {

    @Autowired
    private NotificationsRepo notificationsRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseDTO responseDTO;

    public ResponseDTO sendNotifications(NotificationsDTO notificationsDTO) {
        try {
            NotificationsEntity entity = modelMapper.map(notificationsDTO, NotificationsEntity.class);
            notificationsRepo.save(entity);
            responseDTO.setCode(VarList.RIP_SUCCESS);
            responseDTO.setContent(notificationsDTO);
            responseDTO.setMessage("Successfully send the notification");
        } catch (Exception error) {
            // Log the exception for debugging purposes
            System.err.println("Error sending the notification: " + error.getMessage());
            error.printStackTrace();

            // Explicitly set the transaction to rollback only
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            responseDTO.setCode(VarList.RIP_ERROR);
            responseDTO.setContent(notificationsDTO);
            responseDTO.setMessage("Error sending the notification: " + error.getMessage());
        }
        return responseDTO;
    }
}
