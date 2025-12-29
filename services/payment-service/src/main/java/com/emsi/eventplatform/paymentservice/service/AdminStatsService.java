package com.emsi.eventplatform.paymentservice.service;

import com.emsi.eventplatform.paymentservice.client.EventClient;
import com.emsi.eventplatform.paymentservice.models.enums.PaymentStatus;
import com.emsi.eventplatform.paymentservice.repo.PaymentRepository;
import com.emsi.eventplatform.paymentservice.web.dto.AdminStatsResponse;
import org.springframework.stereotype.Service;

@Service
public class AdminStatsService {

    private final EventClient eventClient;
    private final PaymentRepository paymentRepo;

    public AdminStatsService(EventClient eventClient, PaymentRepository paymentRepo) {
        this.eventClient = eventClient;
        this.paymentRepo = paymentRepo;
    }

    public AdminStatsResponse stats() {
        var events = eventClient.all();
        long totalEvents = events.size();

        long sold = events.stream()
                .mapToLong(e -> (long) e.totalTickets - e.remainingTickets)
                .sum();

        var revenue = paymentRepo.sumSuccessAmount();
        long ok = paymentRepo.countByStatus(PaymentStatus.SUCCESS);
        long fail = paymentRepo.countByStatus(PaymentStatus.FAILED);

        return new AdminStatsResponse(totalEvents, sold, revenue, ok, fail);
    }
}
