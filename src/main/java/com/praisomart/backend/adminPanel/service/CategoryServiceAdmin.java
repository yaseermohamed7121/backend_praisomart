package com.praisomart.backend.adminPanel.service;

import com.praisomart.backend.adminPanel.dto.CategoryRequestDTO;
import com.praisomart.backend.exception.ResourceNotFoundException;
import com.praisomart.backend.products.entity.Category;
import com.praisomart.backend.products.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceAdmin {

    private final CategoryRepository categoryRepository;

    public CategoryServiceAdmin(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Long createCategory(CategoryRequestDTO request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setActive(true);
        return categoryRepository.save(category).getId();
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }
}
