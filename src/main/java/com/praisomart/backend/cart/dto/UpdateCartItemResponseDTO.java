package com.praisomart.backend.cart.dto;

import java.math.BigDecimal;

public class UpdateCartItemResponseDTO {

    private String message;
    private Long cartItemId;
    private int quantity;
    private BigDecimal itemTotal;

    public UpdateCartItemResponseDTO() {}

    public UpdateCartItemResponseDTO(String message, Long cartItemId, int quantity, BigDecimal itemTotal) {
        this.message = message;
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
    }

    public String getMessage() {
        return message;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }
}