package com.emsi.eventplatform.eventservice.web.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class EventResponse {
    public Long id;
    public String title, description, location, organizer, participantA, participantB;
    public OffsetDateTime startAt;
    public BigDecimal ticketPrice;
    public int totalTickets, remainingTickets;

    public EventResponse(Long id, String title, String description, String location,
                         String organizer, String participantA, String participantB,
                         OffsetDateTime startAt, BigDecimal ticketPrice,
                         int totalTickets, int remainingTickets) {
        this.id = id; this.title = title; this.description = description; this.location = location;
        this.organizer = organizer; this.participantA = participantA; this.participantB = participantB;
        this.startAt = startAt; this.ticketPrice = ticketPrice;
        this.totalTickets = totalTickets; this.remainingTickets = remainingTickets;
    }
}
