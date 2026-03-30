package com.praisomart.backend.products.dto;

public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String imageUrl;

    public CategoryResponseDTO(Long id, String name,String imageUrl) {
        this.id = id;
        this.name=name;
        this.imageUrl=imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
