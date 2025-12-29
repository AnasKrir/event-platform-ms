package com.emsi.eventplatform.paymentservice.service;

import com.emsi.eventplatform.paymentservice.client.NotificationClient;
import com.emsi.eventplatform.paymentservice.client.ReservationClient;
import com.emsi.eventplatform.paymentservice.models.*;
import com.emsi.eventplatform.paymentservice.models.enums.PaymentStatus;
import com.emsi.eventplatform.paymentservice.repo.PaymentRepository;
import com.emsi.eventplatform.paymentservice.web.dto.PaymentRequest;
import com.emsi.eventplatform.paymentservice.web.dto.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
public class PaymentAppService {

    private final PaymentRepository repo;
    private final ReservationClient reservationClient;
    private final NotificationClient notification;

    public PaymentAppService(PaymentRepository repo, ReservationClient reservationClient, NotificationClient notification) {
        this.repo = repo; this.reservationClient = reservationClient; this.notification = notification;
    }

    public PaymentResponse toDto(Payment p) {
        return new PaymentResponse(p.getId(), p.getReservationId(), p.getUserId(), p.getAmount(),
                p.getStatus(), p.getMethod(), p.getCreatedAt());
    }

    @Transactional
    @CircuitBreaker(name = "reservationService", fallbackMethod = "payFallback")
    public PaymentResponse pay(Long callerUserId, PaymentRequest req) {
        Map<String, Object> res = reservationClient.get(req.reservationId);
        Long owner = ReservationClient.userId(res);
        String status = ReservationClient.status(res);
        BigDecimal total = ReservationClient.total(res);

        if (!owner.equals(callerUserId)) {
            throw new IllegalArgumentException("Not your reservation");
        }
        if (!"PENDING_PAYMENT".equals(status)) {
            throw new IllegalStateException("Reservation status is " + status);
        }

        Payment p = new Payment();
        p.setReservationId(req.reservationId);
        p.setUserId(callerUserId);
        p.setAmount(total);
        p.setMethod(req.method);
        p.setCreatedAt(OffsetDateTime.now());
        p.setStatus(PaymentStatus.INITIATED);
        p = repo.save(p);

        boolean success = !req.forceFail; // simulation simple
        if (success) {
            p.setStatus(PaymentStatus.SUCCESS);
            repo.save(p);
            reservationClient.markPaid(req.reservationId);

            notification.email(new NotificationClient.EmailRequest(
                    "user-" + callerUserId + "@mail.fake",
                    "Payment success",
                    "Payment #" + p.getId() + " success for reservation #" + req.reservationId + " amount=" + total
            ));
        } else {
            p.setStatus(PaymentStatus.FAILED);
            repo.save(p);
            reservationClient.cancel(req.reservationId);

            notification.sms(new NotificationClient.SmsRequest(
                    "+212000000000",
                    "Payment failed for reservation #" + req.reservationId
            ));
        }

        return toDto(p);
    }

    public PaymentResponse payFallback(Long callerUserId, PaymentRequest req, Throwable ex) {
        throw new IllegalStateException("Reservation service unavailable, please retry later");
    }

    public List<PaymentResponse> myPayments(Long userId) {
        return repo.findByUserId(userId).stream().map(this::toDto).toList();
    }
}
