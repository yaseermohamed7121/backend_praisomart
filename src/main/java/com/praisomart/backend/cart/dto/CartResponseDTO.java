package com.praisomart.backend.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDTO {

    private Long cartId;
    private List<CartItemResponseDTO> items;
    private int totalItems;
    private BigDecimal totalAmount;

    public CartResponseDTO() {
    }

    public CartResponseDTO(
            Long cartId,
            List<CartItemResponseDTO> items,
            int totalItems,
            BigDecimal totalAmount
    ) {
        this.cartId = cartId;
        this.items = items;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
    }

    // getters & setters

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public List<CartItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponseDTO> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}