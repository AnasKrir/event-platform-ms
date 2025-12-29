package com.emsi.eventplatform.userservice.web.controllers;

import com.emsi.eventplatform.userservice.repo.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepo;

    public UserController(UserRepository userRepo) { this.userRepo = userRepo; }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Map<String, Object>> all() {
        return userRepo.findAll().stream().map(u -> Map.of(
                "id", u.getId(),
                "email", u.getEmail(),
                "fullName", u.getFullName(),
                "roles", u.getRoles().stream().map(r -> r.getName().name()).toList()
        )).toList();
    }
}
