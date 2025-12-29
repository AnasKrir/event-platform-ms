package com.emsi.eventplatform.eventservice.web.controllers;


import com.emsi.eventplatform.eventservice.repo.EventRepository;
import com.emsi.eventplatform.eventservice.service.EventAppService;
import com.emsi.eventplatform.eventservice.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/events")
public class InternalEventController {
    private final EventRepository repo;
    private final EventAppService service;

    public InternalEventController(EventRepository repo, EventAppService service) {
        this.repo = repo; this.service = service;
    }

    @GetMapping("/{id}")
    public EventResponse internalGet(@PathVariable Long id) {
        return service.toDto(repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found")));
    }

    @PostMapping("/{id}/hold")
    public HoldTicketsResponse hold(@PathVariable Long id, @Valid @RequestBody HoldTicketsRequest req) {
        return service.holdTickets(id, req.quantity);
    }

    @PostMapping("/{id}/release")
    public void release(@PathVariable Long id, @Valid @RequestBody HoldTicketsRequest req) {
        service.releaseTickets(id, req.quantity);
    }
}
