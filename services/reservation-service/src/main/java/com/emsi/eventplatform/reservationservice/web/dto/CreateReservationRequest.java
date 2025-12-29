package com.emsi.eventplatform.reservationservice.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateReservationRequest {
    @NotNull public Long eventId;
    @Min(1) public int quantity;
}
