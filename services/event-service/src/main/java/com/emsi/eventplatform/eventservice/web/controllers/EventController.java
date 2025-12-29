package com.emsi.eventplatform.eventservice.web.controllers;


import com.emsi.eventplatform.eventservice.repo.EventRepository;
import com.emsi.eventplatform.eventservice.service.EventAppService;
import com.emsi.eventplatform.eventservice.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository repo;
    private final EventAppService service;

    public EventController(EventRepository repo, EventAppService service) {
        this.repo = repo; this.service = service;
    }

    @GetMapping
    public List<EventResponse> all() {
        return repo.findAll().stream().map(service::toDto).toList();
    }

    @GetMapping("/{id}")
    public EventResponse one(@PathVariable Long id) {
        return service.toDto(repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found")));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public EventResponse create(@Valid @RequestBody EventRequest req) {
        return service.toDto(service.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    public EventResponse update(@PathVariable Long id, @Valid @RequestBody EventRequest req) {
        return service.toDto(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ORGANIZER','ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
