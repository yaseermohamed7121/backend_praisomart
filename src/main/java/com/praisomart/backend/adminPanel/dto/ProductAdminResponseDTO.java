package com.praisomart.backend.adminPanel.dto;

import java.util.List;

public class ProductAdminResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String color;
    private Long categoryId;
    private List<ProductVariantDTO> variants;
    private List<ProductImageDTO> images;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public List<ProductVariantDTO> getVariants() { return variants; }
    public void setVariants(List<ProductVariantDTO> variants) { this.variants = variants; }

    public List<ProductImageDTO> getImages() { return images; }
    public void setImages(List<ProductImageDTO> images) { this.images = images; }
}
