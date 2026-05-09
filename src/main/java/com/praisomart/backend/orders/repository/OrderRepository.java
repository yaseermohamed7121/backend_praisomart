package com.praisomart.backend.orders.repository;

import com.praisomart.backend.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'PAID' OR o.status = 'DELIVERED'")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PAID' OR o.status = 'PENDING'")
    long countActiveOrders();
}
