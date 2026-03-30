package com.praisomart.backend.cart.dto;

public class RemoveCartItemResponseDTO {

    private String message;
    private Long cartItemId;

    public RemoveCartItemResponseDTO() {}

    public RemoveCartItemResponseDTO(String message, Long cartItemId) {
        this.message = message;
        this.cartItemId = cartItemId;
    }

    public String getMessage() {
        return message;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}