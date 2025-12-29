package com.emsi.eventplatform.eventservice.service;

import com.emsi.eventplatform.eventservice.models.Event;
import com.emsi.eventplatform.eventservice.repo.EventRepository;
import com.emsi.eventplatform.eventservice.web.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EventAppService {
    private final EventRepository repo;

    public EventAppService(EventRepository repo) { this.repo = repo; }

    public EventResponse toDto(Event e) {
        return new EventResponse(
                e.getId(), e.getTitle(), e.getDescription(), e.getLocation(),
                e.getOrganizer(), e.getParticipantA(), e.getParticipantB(),
                e.getStartAt(), e.getTicketPrice(), e.getTotalTickets(), e.getRemainingTickets()
        );
    }

    public Event create(EventRequest r) {
        Event e = new Event();
        e.setTitle(r.title);
        e.setDescription(r.description);
        e.setLocation(r.location);
        e.setOrganizer(r.organizer);
        e.setParticipantA(r.participantA);
        e.setParticipantB(r.participantB);
        e.setStartAt(r.startAt);
        e.setTicketPrice(r.ticketPrice);
        e.setTotalTickets(r.totalTickets);
        e.setRemainingTickets(r.totalTickets);
        return repo.save(e);
    }

    public Event update(Long id, EventRequest r) {
        Event e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        e.setTitle(r.title);
        e.setDescription(r.description);
        e.setLocation(r.location);
        e.setOrganizer(r.organizer);
        e.setParticipantA(r.participantA);
        e.setParticipantB(r.participantB);
        e.setStartAt(r.startAt);
        e.setTicketPrice(r.ticketPrice);

        // si on change totalTickets, on ajuste remainingTickets sans descendre sous 0
        int oldTotal = e.getTotalTickets();
        int delta = r.totalTickets - oldTotal;
        e.setTotalTickets(r.totalTickets);
        e.setRemainingTickets(Math.max(0, e.getRemainingTickets() + delta));
        return repo.save(e);
    }

    public void delete(Long id) { repo.deleteById(id); }

    @Transactional
    public HoldTicketsResponse holdTickets(Long eventId, int qty) {
        Event e = repo.findByIdForUpdate(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        if (e.getRemainingTickets() < qty) throw new IllegalStateException("Not enough tickets");
        e.setRemainingTickets(e.getRemainingTickets() - qty);
        repo.save(e);

        BigDecimal total = e.getTicketPrice().multiply(BigDecimal.valueOf(qty));
        return new HoldTicketsResponse(e.getId(), qty, e.getRemainingTickets(), e.getTicketPrice(), total);
    }

    @Transactional
    public void releaseTickets(Long eventId, int qty) {
        Event e = repo.findByIdForUpdate(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        int newRemaining = Math.min(e.getTotalTickets(), e.getRemainingTickets() + qty);
        e.setRemainingTickets(newRemaining);
        repo.save(e);
    }
}
