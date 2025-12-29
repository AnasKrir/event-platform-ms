package com.emsi.eventplatform.reservationservice.repo;


import com.emsi.eventplatform.reservationservice.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    Optional<Reservation> findByIdAndUserId(Long id, Long userId);
}
