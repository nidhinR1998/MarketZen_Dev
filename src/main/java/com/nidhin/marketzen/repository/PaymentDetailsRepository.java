package com.nidhin.marketzen.repository;

import com.nidhin.marketzen.models.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {
    PaymentDetails findByUserId(Long userId);
}
