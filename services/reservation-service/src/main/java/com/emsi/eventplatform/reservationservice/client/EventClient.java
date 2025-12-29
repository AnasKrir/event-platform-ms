package com.emsi.eventplatform.reservationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@FeignClient(name = "event-service", path = "/internal/events", configuration = FeignInternalAuthConfig.class)
public interface EventClient {

    @PostMapping("/{id}/hold")
    HoldResponse hold(@PathVariable("id") Long eventId, @Valid @RequestBody HoldRequest req);

    @PostMapping("/{id}/release")
    void release(@PathVariable("id") Long eventId, @Valid @RequestBody HoldRequest req);

    class HoldRequest {
        @Min(1) public int quantity;
        public HoldRequest() {}
        public HoldRequest(int quantity) { this.quantity = quantity; }
    }

    class HoldResponse {
        public Long eventId;
        public int requested;
        public int remainingAfter;
        public BigDecimal unitPrice;
        public BigDecimal total;
    }
}
