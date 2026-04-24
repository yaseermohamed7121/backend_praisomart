package com.praisomart.backend.products.service;

import com.praisomart.backend.products.dto.CategoryResponseDTO;
import com.praisomart.backend.products.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository=categoryRepository;
    }

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findByIsActiveTrueOrderByNameAsc()
                .stream()
                .map(c -> new CategoryResponseDTO(
                        c.getId(),
                        c.getName(),
                        c.getImageUrl()
                ))
                .toList();
    }
}
