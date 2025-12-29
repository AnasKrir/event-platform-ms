package com.emsi.eventplatform.eventservice.web.dto;

import java.math.BigDecimal;

public class HoldTicketsResponse {
    public Long eventId;
    public int requested;
    public int remainingAfter;
    public BigDecimal unitPrice;
    public BigDecimal total;

    public HoldTicketsResponse(Long eventId, int requested, int remainingAfter, BigDecimal unitPrice, BigDecimal total) {
        this.eventId = eventId; this.requested = requested; this.remainingAfter = remainingAfter;
        this.unitPrice = unitPrice; this.total = total;
    }
}