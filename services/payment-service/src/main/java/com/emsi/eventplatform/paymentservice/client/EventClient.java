package com.emsi.eventplatform.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@FeignClient(name = "event-service", path = "/api/events")
public interface EventClient {

    @GetMapping
    List<EventDto> all();

    class EventDto {
        public Long id;
        public String title;
        public String organizer;
        public String participantA;
        public String participantB;
        public OffsetDateTime startAt;
        public BigDecimal ticketPrice;
        public int totalTickets;
        public int remainingTickets;
    }
}
