package com.emsi.eventplatform.reservationservice.service;

import com.emsi.eventplatform.reservationservice.client.EventClient;
import com.emsi.eventplatform.reservationservice.client.NotificationClient;
import com.emsi.eventplatform.reservationservice.models.*;
import com.emsi.eventplatform.reservationservice.models.enums.*;
import com.emsi.eventplatform.reservationservice.repo.ReservationRepository;
import com.emsi.eventplatform.reservationservice.web.dto.CreateReservationRequest;
import com.emsi.eventplatform.reservationservice.web.dto.ReservationResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ReservationAppService {

    private final ReservationRepository repo;
    private final EventClient eventClient;
    private final NotificationClient notification;

    public ReservationAppService(ReservationRepository repo, EventClient eventClient, NotificationClient notification) {
        this.repo = repo; this.eventClient = eventClient; this.notification = notification;
    }

    public ReservationResponse toDto(Reservation r) {
        return new ReservationResponse(
                r.getId(), r.getEventId(), r.getUserId(), r.getQuantity(),
                r.getUnitPrice(), r.getTotalPrice(), r.getStatus(), r.getCreatedAt()
        );
    }

    @Transactional
    @CircuitBreaker(name = "eventService", fallbackMethod = "createFallback")
    public ReservationResponse create(Long userId, CreateReservationRequest req) {
        if (req.quantity > 4) {
            throw new IllegalArgumentException("Max 4 tickets per reservation");
        }

        EventClient.HoldResponse hold = eventClient.hold(req.eventId, new EventClient.HoldRequest(req.quantity));

        Reservation r = new Reservation();
        r.setUserId(userId);
        r.setEventId(req.eventId);
        r.setQuantity(req.quantity);
        r.setUnitPrice(hold.unitPrice);
        r.setTotalPrice(hold.total);
        r.setStatus(ReservationStatus.PENDING_PAYMENT);
        r.setCreatedAt(OffsetDateTime.now());

        Reservation saved = repo.save(r);

        // notif simulée
        notification.email(new NotificationClient.EmailRequest(
                "user-" + userId + "@mail.fake",
                "Reservation created",
                "Reservation #" + saved.getId() + " for event " + saved.getEventId() + " total=" + saved.getTotalPrice()
        ));

        return toDto(saved);
    }

    // fallback Resilience4j
    public ReservationResponse createFallback(Long userId, CreateReservationRequest req, Throwable ex) {
        throw new IllegalStateException("Event service unavailable, please retry later");
    }

    public List<ReservationResponse> myReservations(Long userId) {
        return repo.findByUserId(userId).stream().map(this::toDto).toList();
    }

    public Reservation getOwnedOrThrow(Long id, Long userId) {
        return repo.findByIdAndUserId(id, userId).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    @Transactional
    public void markPaidInternal(Long id) {
        Reservation r = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (r.getStatus() == ReservationStatus.PAID) return;
        if (r.getStatus() == ReservationStatus.CANCELED) throw new IllegalStateException("Reservation canceled");
        r.setStatus(ReservationStatus.PAID);
        repo.save(r);
    }

    @Transactional
    public void cancelInternal(Long id) {
        Reservation r = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if (r.getStatus() == ReservationStatus.CANCELED) return;

        // release tickets if not paid
        if (r.getStatus() != ReservationStatus.PAID) {
            eventClient.release(r.getEventId(), new EventClient.HoldRequest(r.getQuantity()));
        }
        r.setStatus(ReservationStatus.CANCELED);
        repo.save(r);

        notification.sms(new NotificationClient.SmsRequest(
                "+212000000000",
                "Reservation #" + r.getId() + " canceled"
        ));
    }
}
