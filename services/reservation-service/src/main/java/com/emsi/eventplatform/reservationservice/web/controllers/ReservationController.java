package com.emsi.eventplatform.reservationservice.web.controllers;


import com.emsi.eventplatform.reservationservice.service.ReservationAppService;
import com.emsi.eventplatform.reservationservice.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationAppService service;

    public ReservationController(ReservationAppService service) { this.service = service; }

    private Long userId(Authentication auth) { return Long.valueOf(auth.getName()); } // subject = userId

    @PostMapping
    public ReservationResponse create(Authentication auth, @Valid @RequestBody CreateReservationRequest req) {
        return service.create(userId(auth), req);
    }

    @GetMapping("/my")
    public List<ReservationResponse> my(Authentication auth) {
        return service.myReservations(userId(auth));
    }
}
