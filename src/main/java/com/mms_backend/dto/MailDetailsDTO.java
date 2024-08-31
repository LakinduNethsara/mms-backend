package com.mms_backend.dto;

import com.mms_backend.entity.NotificationsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDetailsDTO {
    private String toMail;
    private String subject;
    private String message;
    private String fromMail;
    private List<NotificationsEntity> notificationsDTOList;
}