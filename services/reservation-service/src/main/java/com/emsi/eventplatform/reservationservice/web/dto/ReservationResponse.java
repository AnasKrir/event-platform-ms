package com.emsi.eventplatform.reservationservice.web.dto;

import com.emsi.eventplatform.reservationservice.models.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ReservationResponse {
    public Long id, eventId, userId;
    public int quantity;
    public BigDecimal unitPrice, totalPrice;
    public ReservationStatus status;
    public OffsetDateTime createdAt;

    public ReservationResponse(Long id, Long eventId, Long userId, int quantity,
                               BigDecimal unitPrice, BigDecimal totalPrice,
                               ReservationStatus status, OffsetDateTime createdAt) {
        this.id=id; this.eventId=eventId; this.userId=userId; this.quantity=quantity;
        this.unitPrice=unitPrice; this.totalPrice=totalPrice; this.status=status; this.createdAt=createdAt;
    }
}
