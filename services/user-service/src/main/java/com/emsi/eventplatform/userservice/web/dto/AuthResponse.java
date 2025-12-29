package com.emsi.eventplatform.userservice.web.dto;

import java.util.List;

public class AuthResponse {
    public String token;
    public Long userId;
    public String email;
    public List<String> roles;

    public AuthResponse(String token, Long userId, String email, List<String> roles) {
        this.token = token; this.userId = userId; this.email = email; this.roles = roles;
    }
}
