package com.example.gotogether.controllers;

import com.example.gotogether.dto.SupportRequestDTO;
import com.example.gotogether.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private  EmailService emailService;

    @PostMapping("/message")
    public ResponseEntity<String> sendSupportMessage(@RequestBody SupportRequestDTO request) {
        emailService.sendSupportMessage(
                request.getFullName(),
                request.getEmail(),
                request.getCategory(),
                request.getSubject(),
                request.getMessage()
        );

        return ResponseEntity.ok("Support message sent.");
    }
}
