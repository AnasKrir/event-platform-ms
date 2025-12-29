package com.emsi.eventplatform.paymentservice.web.controllers;


import com.emsi.eventplatform.paymentservice.service.PaymentAppService;
import com.emsi.eventplatform.paymentservice.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentAppService service;

    public PaymentController(PaymentAppService service) { this.service = service; }

    private Long userId(Authentication auth) { return Long.valueOf(auth.getName()); }

    @PostMapping
    public PaymentResponse pay(Authentication auth, @Valid @RequestBody PaymentRequest req) {
        return service.pay(userId(auth), req);
    }

    @GetMapping("/my")
    public List<PaymentResponse> my(Authentication auth) {
        return service.myPayments(userId(auth));
    }
}
