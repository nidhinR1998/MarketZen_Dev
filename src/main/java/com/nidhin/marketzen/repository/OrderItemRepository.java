package com.nidhin.marketzen.repository;

import com.nidhin.marketzen.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
