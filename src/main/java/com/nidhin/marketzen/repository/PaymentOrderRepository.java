package com.nidhin.marketzen.repository;

import com.nidhin.marketzen.models.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {

}
