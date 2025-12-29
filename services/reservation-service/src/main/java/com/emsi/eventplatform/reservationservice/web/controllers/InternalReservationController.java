package com.emsi.eventplatform.reservationservice.web.controllers;


import com.emsi.eventplatform.reservationservice.models.Reservation;
import com.emsi.eventplatform.reservationservice.repo.ReservationRepository;
import com.emsi.eventplatform.reservationservice.service.ReservationAppService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/internal/reservations")
public class InternalReservationController {

    private final ReservationRepository repo;
    private final ReservationAppService service;

    public InternalReservationController(ReservationRepository repo, ReservationAppService service) {
        this.repo = repo; this.service = service;
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable Long id) {
        Reservation r = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        return Map.of(
                "id", r.getId(),
                "eventId", r.getEventId(),
                "userId", r.getUserId(),
                "quantity", r.getQuantity(),
                "unitPrice", r.getUnitPrice(),
                "totalPrice", r.getTotalPrice(),
                "status", r.getStatus().name()
        );
    }

    @PostMapping("/{id}/mark-paid")
    public void markPaid(@PathVariable Long id) {
        service.markPaidInternal(id);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        service.cancelInternal(id);
    }
}
