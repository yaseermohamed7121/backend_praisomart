package com.praisomart.backend.adminPanel.service;

import com.praisomart.backend.orders.repository.OrderRepository;
import com.praisomart.backend.products.repository.ProductRepository;
import com.praisomart.backend.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminDashboardService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalProducts = productRepository.count();
        BigDecimal totalRevenue = orderRepository.calculateTotalRevenue();
        long activeOrders = orderRepository.countActiveOrders();
        long totalCustomers = userRepository.countByRole("USER");

        stats.put("totalProducts", totalProducts);
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        stats.put("activeOrders", activeOrders);
        stats.put("totalCustomers", totalCustomers);

        return stats;
    }
}
