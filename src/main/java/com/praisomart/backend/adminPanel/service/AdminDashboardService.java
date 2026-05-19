package com.praisomart.backend.adminPanel.service;

import com.praisomart.backend.adminPanel.dto.OrderStatusUpdateRequestDTO;
import com.praisomart.backend.adminPanel.dto.OrderStatusUpdateResponseDTO;
import com.praisomart.backend.exception.BadRequestException;
import com.praisomart.backend.exception.ResourceNotFoundException;
import com.praisomart.backend.orders.constants.OrderStatus;
import com.praisomart.backend.orders.entity.Order;
import com.praisomart.backend.orders.repository.OrderRepository;
import com.praisomart.backend.products.repository.ProductRepository;
import com.praisomart.backend.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public OrderStatusUpdateResponseDTO updateOrderStatus(
            Long orderId,
            OrderStatusUpdateRequestDTO request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        String currentStatus = order.getStatus();
        String newStatus = request.getStatus().trim().toUpperCase();

        // 1. validate status exists
        if (!OrderStatus.VALID_STATUSES.contains(newStatus)) {
            throw new BadRequestException(
                    "Invalid status. Allowed: PENDING, PACKED, SHIPPED, DELIVERED"
            );
        }

        // 2. validate flow
        validateStatusFlow(currentStatus, newStatus);

        // 3. update
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        // 4. response
        OrderStatusUpdateResponseDTO response = new OrderStatusUpdateResponseDTO();
        response.setOrderId(order.getId());
        response.setPreviousStatus(currentStatus);
        response.setUpdatedStatus(newStatus);
        response.setUpdatedAt(order.getUpdatedAt());
        response.setMessage("Order status updated successfully");

        return response;
    }

    private void validateStatusFlow(String current, String next) {

        if (OrderStatus.PENDING.equals(current)
                && !OrderStatus.PACKED.equals(next)) {
            throw new BadRequestException("PENDING → only PACKED allowed");
        }

        if (OrderStatus.PACKED.equals(current)
                && !OrderStatus.SHIPPED.equals(next)) {
            throw new BadRequestException("PACKED → only SHIPPED allowed");
        }

        if (OrderStatus.SHIPPED.equals(current)
                && !OrderStatus.DELIVERED.equals(next)) {
            throw new BadRequestException("SHIPPED → only DELIVERED allowed");
        }

        if (OrderStatus.DELIVERED.equals(current)) {
            throw new BadRequestException("DELIVERED cannot be changed");
        }
    }
}
