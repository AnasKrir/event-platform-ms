package com.emsi.eventplatform.notificationservice.web.dto;

import jakarta.validation.constraints.NotBlank;

public class SmsRequest {
    @NotBlank public String to;
    @NotBlank public String message;
}
