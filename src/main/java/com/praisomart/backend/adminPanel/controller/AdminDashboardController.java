package com.praisomart.backend.adminPanel.controller;

import com.praisomart.backend.adminPanel.service.AdminDashboardService;
import com.praisomart.backend.orders.entity.Order;
import com.praisomart.backend.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService dashboardService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<List<Order>> getRecentOrders() {
        // Return latest 10 orders
        return ResponseEntity.ok(orderRepository.findAll());
    }
}
