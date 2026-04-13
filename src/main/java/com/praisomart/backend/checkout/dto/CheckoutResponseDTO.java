package com.praisomart.backend.checkout.dto;

import com.praisomart.backend.cart.dto.CartItemResponseDTO;
import com.praisomart.backend.address.dto.AddressResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutResponseDTO {

    private Long cartId;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalAmount;
    private List<AddressResponseDTO> addresses;

    public CheckoutResponseDTO() {}

    public CheckoutResponseDTO(Long cartId,
                               List<CartItemResponseDTO> items,
                               BigDecimal totalAmount,
                               List<AddressResponseDTO> addresses) {
        this.cartId = cartId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.addresses = addresses;
    }

    public List<AddressResponseDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressResponseDTO> addresses) {
        this.addresses = addresses;
    }

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

}