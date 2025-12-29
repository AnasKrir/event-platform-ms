package com.emsi.eventplatform.userservice.service;

import com.emsi.eventplatform.userservice.models.*;
import com.emsi.eventplatform.userservice.models.enums.*;
import com.emsi.eventplatform.userservice.repo.*;
import com.emsi.eventplatform.userservice.security.JwtService;
import com.emsi.eventplatform.userservice.web.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder, JwtService jwt) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email)) {
            throw new IllegalArgumentException("Email already used");
        }

        RoleName roleName = RoleName.USER;
        if (req.role != null && req.role.equalsIgnoreCase("ORGANIZER")) roleName = RoleName.ORGANIZER;
        if (req.role != null && req.role.equalsIgnoreCase("ADMIN")) {
            throw new IllegalArgumentException("ADMIN cannot be created via register");
        }

        Role role = roleRepo.findByName(roleName).orElseThrow();

        AppUser u = new AppUser();
        u.setEmail(req.email.toLowerCase());
        u.setFullName(req.fullName);
        u.setPasswordHash(encoder.encode(req.password));
        u.getRoles().add(role);

        AppUser saved = userRepo.save(u);

        List<String> roles = saved.getRoles().stream().map(r -> r.getName().name()).toList();
        String token = jwt.generateToken(saved.getId().toString(), saved.getEmail(), roles);
        return new AuthResponse(token, saved.getId(), saved.getEmail(), roles);
    }

    public AuthResponse login(LoginRequest req) {
        AppUser u = userRepo.findByEmail(req.email.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!encoder.matches(req.password, u.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        List<String> roles = u.getRoles().stream().map(r -> r.getName().name()).toList();
        String token = jwt.generateToken(u.getId().toString(), u.getEmail(), roles);
        return new AuthResponse(token, u.getId(), u.getEmail(), roles);
    }
}
