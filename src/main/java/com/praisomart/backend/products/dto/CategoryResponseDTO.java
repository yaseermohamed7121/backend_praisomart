package com.praisomart.backend.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String imageUrl;

    public CategoryResponseDTO(Long id, String imageUrl, String name) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
    }
}
