package com.emsi.eventplatform.paymentservice.repo;

import com.emsi.eventplatform.paymentservice.models.Payment;
import com.emsi.eventplatform.paymentservice.models.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);

    long countByStatus(PaymentStatus status);

    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.status = com.emsi.eventplatform.paymentservice.models.enums.PaymentStatus.SUCCESS")
    BigDecimal sumSuccessAmount();
}
