package com.emsi.eventplatform.paymentservice.web.dto;

import com.emsi.eventplatform.paymentservice.models.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class PaymentResponse {
    public Long id, reservationId, userId;
    public BigDecimal amount;
    public PaymentStatus status;
    public String method;
    public OffsetDateTime createdAt;

    public PaymentResponse(Long id, Long reservationId, Long userId, BigDecimal amount,
                           PaymentStatus status, String method, OffsetDateTime createdAt) {
        this.id=id; this.reservationId=reservationId; this.userId=userId;
        this.amount=amount; this.status=status; this.method=method; this.createdAt=createdAt;
    }
}
