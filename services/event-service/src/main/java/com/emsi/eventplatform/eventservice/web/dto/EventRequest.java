package com.emsi.eventplatform.eventservice.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class EventRequest {
    @NotBlank public String title;
    public String description;
    public String location;

    @NotBlank public String organizer;
    @NotBlank public String participantA;
    @NotBlank public String participantB;

    @NotNull public OffsetDateTime startAt;

    @NotNull @DecimalMin("0.0")
    public BigDecimal ticketPrice;

    @Min(1) public int totalTickets = 40000; // default exemple
}
