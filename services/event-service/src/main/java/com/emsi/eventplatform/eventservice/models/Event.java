package com.emsi.eventplatform.eventservice.models;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;
    private String location;

    @Column(nullable = false)
    private String organizer;

    @Column(nullable = false)
    private String participantA;

    @Column(nullable = false)
    private String participantB;

    @Column(nullable = false)
    private OffsetDateTime startAt;

    @Column(nullable = false)
    private BigDecimal ticketPrice;

    @Column(nullable = false)
    private int totalTickets;

    @Column(nullable = false)
    private int remainingTickets;

    @Version
    private Long version;
}
