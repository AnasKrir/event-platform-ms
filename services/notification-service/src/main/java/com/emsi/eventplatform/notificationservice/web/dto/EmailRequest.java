package com.emsi.eventplatform.notificationservice.web.dto;


import jakarta.validation.constraints.NotBlank;

public class EmailRequest {
    @NotBlank public String to;
    @NotBlank public String subject;
    @NotBlank public String message;
}
