package com.mms_backend.service;

import com.mms_backend.dto.MailDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServerService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(MailDetailsDTO mailDetailsDTO) throws MessagingException {
        // Create a Thymeleaf context and add variables to it
        Context context = new Context();
        context.setVariable("message", mailDetailsDTO.getMessage());

        // Check if the notifications list is not null and not empty
        if (mailDetailsDTO.getNotificationsDTOList() != null && !mailDetailsDTO.getNotificationsDTOList().isEmpty()) {
            context.setVariable("notifications", mailDetailsDTO.getNotificationsDTOList());
        }

        // Process the template and generate the email content
        String body = templateEngine.process("email-template.html", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setText(body, true); // Set to true as the email content is HTML
        helper.setTo(mailDetailsDTO.getToMail());
        helper.setSubject(mailDetailsDTO.getSubject());
        helper.setFrom("mms@fot.ruh.ac.lk");

        // Send the email
        mailSender.send(mimeMessage);
    }
}
