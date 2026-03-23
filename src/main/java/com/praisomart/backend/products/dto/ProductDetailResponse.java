package com.praisomart.backend.products.dto;

import java.util.List;

public class ProductDetailResponse {

    private Long id;
    private String name;
    private String description;
    private String color;
    private List<String> images;
    private List<VariantResponse> variants;

    // ✅ Constructor (correct order)
    public ProductDetailResponse(Long id, String name, String description,
                                 String color, List<String> images,
                                 List<VariantResponse> variants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.images = images;
        this.variants = variants;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }

    public List<VariantResponse> getVariants() { return variants; }
    public void setVariants(List<VariantResponse> variants) { this.variants = variants; }
}