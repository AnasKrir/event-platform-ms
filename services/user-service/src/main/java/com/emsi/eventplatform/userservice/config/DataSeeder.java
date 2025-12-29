package com.emsi.eventplatform.userservice.config;

import com.emsi.eventplatform.userservice.models.*;
import com.emsi.eventplatform.userservice.models.enums.*;
import com.emsi.eventplatform.userservice.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            for (RoleName rn : RoleName.values()) {
                roleRepo.findByName(rn)
                        .orElseGet(() -> roleRepo.save(Role.builder().name(rn).build()));
            }

            // admin par défaut (change le mdp)
            String adminEmail = "admin@event.com";
            userRepo.findByEmail(adminEmail).orElseGet(() -> {
                Role adminRole = roleRepo.findByName(RoleName.ADMIN).orElseThrow();
                AppUser u = new AppUser();
                u.setEmail(adminEmail);
                u.setFullName("Admin");
                u.setPasswordHash(encoder.encode("Admin123!"));
                u.getRoles().add(adminRole);
                return userRepo.save(u);
            });
        };
    }
}
