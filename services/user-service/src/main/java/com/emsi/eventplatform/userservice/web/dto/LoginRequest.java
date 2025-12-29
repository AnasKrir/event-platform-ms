package com.emsi.eventplatform.userservice.web.dto;

import jakarta.validation.constraints.*;

public class LoginRequest {
    @Email @NotBlank
    public String email;

    @NotBlank
    public String password;
}
