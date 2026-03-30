package com.praisomart.backend.cart.dto;

import java.math.BigDecimal;

public class CartItemResponseDTO {

    private Long cartItemId;
    private Long productId;
    private Long productVariantId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    public CartItemResponseDTO() {
    }

    public CartItemResponseDTO(
            Long cartItemId,
            Long productId,
            Long productVariantId,
            String productName,
            String imageUrl,
            int quantity,
            BigDecimal price,
            BigDecimal totalPrice
    ) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productVariantId = productVariantId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(Long productVariantId) {
        this.productVariantId = productVariantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}