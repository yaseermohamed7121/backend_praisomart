package com.praisomart.backend.products.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_variants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "size"}))
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;

    private BigDecimal price;

    private Integer stock = 0;

    private String sku;

    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Constructor
    public ProductVariant() {}

    public ProductVariant(Long id, String size, BigDecimal price,
                          Integer stock, String sku, Boolean isActive,
                          Product product) {
        this.id = id;
        this.size = size;
        this.price = price;
        this.stock = stock;
        this.sku = sku;
        this.isActive = isActive;
        this.product = product;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}