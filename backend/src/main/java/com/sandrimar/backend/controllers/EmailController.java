package com.sandrimar.backend.controllers;

import com.sandrimar.backend.services.EmailService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin
public class EmailController {

    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public void sendEmail(@Email
                          @NotBlank(message = "Email is mandatory")
                          @RequestParam(value = "to") String toEmail) {
        service.setRecipientEmail(toEmail);
    }
}
