package com.mms_backend.controller;

import com.mms_backend.dto.MailDetailsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mail")
public class MailController {


    @PostMapping("/send")
    public String sendMail(@RequestBody MailDetailsDTO mailDetailsDTO) {


        return null;
    }
}

