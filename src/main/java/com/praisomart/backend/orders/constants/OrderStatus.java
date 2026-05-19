package com.praisomart.backend.orders.constants;

import java.util.Set;

public class OrderStatus {

    public static final String PENDING = "PENDING";
    public static final String PACKED = "PACKED";
    public static final String SHIPPED = "SHIPPED";
    public static final String DELIVERED = "DELIVERED";

    public static final Set<String> VALID_STATUSES = Set.of(
            PENDING,
            PACKED,
            SHIPPED,
            DELIVERED
    );
}