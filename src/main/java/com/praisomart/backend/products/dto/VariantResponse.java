package com.praisomart.backend.products.dto;

public class VariantResponse {

    private String size;
    private Double price;
    private Integer stock;

    // ✅ Constructor (correct order)
    public VariantResponse(String size, Double price, Integer stock) {
        this.size = size;
        this.price = price;
        this.stock = stock;
    }

    // Getters & Setters
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}