package com.mms_backend.service;

import com.mms_backend.dto.MailDetailsDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
public class MailServerService {

    @Autowired
    private JavaMailSender javaMailSender;

    public String sendMail( MailDetailsDTO mailDetailsDTO) {

        try {
            if (mailDetailsDTO.getToMail() == null || mailDetailsDTO.getToMail().isEmpty()) {
                return "To mail is required";
            }
            if (mailDetailsDTO.getSubject() == null || mailDetailsDTO.getSubject().isEmpty()) {
                return "Subject is required";
            }
            if (mailDetailsDTO.getMessage() == null || mailDetailsDTO.getMessage().isEmpty()) {
                return "Message is required";
            }
            if (mailDetailsDTO.getFromMail() == null || mailDetailsDTO.getFromMail().isEmpty()) {
                return "From mail is required";
            }

            SimpleMailMessage message = new SimpleMailMessage();



            message.setFrom(mailDetailsDTO.getFromMail()); // Set from mail

            message.setTo(mailDetailsDTO.getToMail()); // Set to mail
            message.setSubject(mailDetailsDTO.getSubject()); // Set subject of mail
            message.setText(mailDetailsDTO.getMessage()); // Set message of mail

            // Send mail
            javaMailSender.send(message);

            return "Mail sent successfully";

        } catch (Exception e) {
            return "Error occurred while sending mail"+e.getMessage();
        }
    }

}
