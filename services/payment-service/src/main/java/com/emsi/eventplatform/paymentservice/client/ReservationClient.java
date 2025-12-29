package com.emsi.eventplatform.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(name = "reservation-service", path = "/internal/reservations", configuration = FeignInternalAuthConfig.class)
public interface ReservationClient {

    @GetMapping("/{id}")
    Map<String, Object> get(@PathVariable("id") Long reservationId);

    @PostMapping("/{id}/mark-paid")
    void markPaid(@PathVariable("id") Long reservationId);

    @PostMapping("/{id}/cancel")
    void cancel(@PathVariable("id") Long reservationId);

    static BigDecimal total(Map<String, Object> m) {
        Object v = m.get("totalPrice");
        if (v == null) return BigDecimal.ZERO;
        return new BigDecimal(String.valueOf(v));
    }

    static Long userId(Map<String, Object> m) {
        return Long.valueOf(String.valueOf(m.get("userId")));
    }

    static String status(Map<String, Object> m) {
        return String.valueOf(m.get("status"));
    }
}
