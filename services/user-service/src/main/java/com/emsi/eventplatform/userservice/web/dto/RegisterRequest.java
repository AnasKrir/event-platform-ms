package com.emsi.eventplatform.userservice.web.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {
    @Email @NotBlank
    public String email;

    @NotBlank @Size(min = 6)
    public String password;

    @NotBlank
    public String fullName;

    // "USER" ou "ORGANIZER" (ADMIN non autorisé par register)
    public String role;
}
