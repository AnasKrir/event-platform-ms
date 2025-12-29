package com.emsi.eventplatform.paymentservice.web.controllers;

import com.emsi.eventplatform.paymentservice.service.AdminStatsService;
import com.emsi.eventplatform.paymentservice.web.dto.AdminStatsResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/admin")
public class AdminStatsController {

    private final AdminStatsService service;

    public AdminStatsController(AdminStatsService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public AdminStatsResponse stats() {
        return service.stats();
    }
}
