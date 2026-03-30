package com.praisomart.backend.cart.dto;

public class AddCartItemResponseDTO {
    private String message;

    private Long cartItemId;

    private Integer quantity;

    public AddCartItemResponseDTO() {
    }

    public AddCartItemResponseDTO( String message,Long cartItemId, Integer quantity) {
        this.message = message;
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
