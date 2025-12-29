package com.emsi.eventplatform.notificationservice.web.controllers;


import com.emsi.eventplatform.notificationservice.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @PostMapping("/email")
    public void email(@Valid @RequestBody EmailRequest req) {
        System.out.println("[EMAIL] to=" + req.to + " subject=" + req.subject + " msg=" + req.message);
    }

    @PostMapping("/sms")
    public void sms(@Valid @RequestBody SmsRequest req) {
        System.out.println("[SMS] to=" + req.to + " msg=" + req.message);
    }
}
