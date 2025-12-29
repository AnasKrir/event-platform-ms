package com.emsi.eventplatform.paymentservice.web.dto;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
    @NotNull public Long reservationId;
    public String method = "SIMULATED_CARD";
    public boolean forceFail = false; // pour tester échec
}
