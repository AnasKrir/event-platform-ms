package com.emsi.eventplatform.paymentservice.web.dto;

import java.math.BigDecimal;

public record AdminStatsResponse(
        long totalEvents,
        long ticketsSold,
        BigDecimal revenue,
        long paymentsSuccess,
        long paymentsFailed
) {}
