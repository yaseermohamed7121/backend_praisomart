package com.praisomart.backend.products.dto;

public class VariantResponse {

    private String attribute;
    private Double price;
    private Integer stock;

    // ✅ Constructor (correct order)
    public VariantResponse(String attribute, Double price, Integer stock) {
        this.attribute = attribute;
        this.price = price;
        this.stock = stock;
    }

    // Getters & Setters
    public String getSize() { return attribute; }
    public void setSize(String size) { this.attribute = attribute; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}