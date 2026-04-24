package com.praisomart.backend.adminPanel.service;

import com.praisomart.backend.adminPanel.dto.ProductImageDTO;
import com.praisomart.backend.adminPanel.dto.ProductRequestDTO;
import com.praisomart.backend.adminPanel.dto.ProductVariantDTO;
import com.praisomart.backend.exception.BadRequestException;
import com.praisomart.backend.exception.ResourceNotFoundException;
import com.praisomart.backend.products.entity.Category;
import com.praisomart.backend.products.entity.Product;
import com.praisomart.backend.products.entity.ProductImage;
import com.praisomart.backend.products.entity.ProductVariant;
import com.praisomart.backend.products.repository.CategoryRepository;
import com.praisomart.backend.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ProductServiceAdmin {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceAdmin(ProductRepository productRepository,
                               CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Long createProduct(ProductRequestDTO request) {

        // 1. Basic Validation
        if (request.getName() == null || request.getName().isBlank()) {
            throw new BadRequestException("Product name is required");
        }

        if (request.getCategoryId() == null) {
            throw new BadRequestException("Category is required");
        }

        if (request.getVariants() == null || request.getVariants().isEmpty()) {
            throw new BadRequestException("At least one variant is required");
        }

        // 2. Fetch Category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // 3. Create Product
        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setColor(request.getColor());
        product.setCategory(category);
        product.setActive(true);

        //  4. Map Variants
        Set<ProductVariant> variants = new HashSet<>();

        for (ProductVariantDTO v : request.getVariants()) {

            if (v.getPrice() == null) {
                throw new BadRequestException("Variant price is required");
            }

            ProductVariant variant = new ProductVariant();

            // ATTRIBUTE
            String attribute = (v.getAttribute() == null || v.getAttribute().isBlank())
                    ? "DEFAULT"
                    : v.getAttribute().trim();

            variant.setAttribute(attribute);

            variant.setPrice(v.getPrice());
            variant.setStock(v.getStock() != null ? v.getStock() : 0);
            variant.setIsActive(true);

            // SKU GENERATION
            String baseName = request.getName().toUpperCase().replaceAll(" ", "-");
            String color = request.getColor() != null ? request.getColor().toUpperCase() : "NA";

            String generatedSku = baseName + "-" + color + "-" + attribute.toUpperCase()
                    + "-" + System.currentTimeMillis();

            variant.setSku(generatedSku);

            variant.setProduct(product);

            variants.add(variant);
        }

        // 5. Map Images
        Set<ProductImage> images = new HashSet<>();

        if (request.getImages() != null) {

            boolean hasPrimary = false;

            for (ProductImageDTO i : request.getImages()) {

                if (i.getImageUrl() == null || i.getImageUrl().isBlank()) {
                    continue;
                }

                ProductImage image = new ProductImage();
                image.setImageUrl(i.getImageUrl().trim());
                image.setPrimary(i.getIsPrimary() != null && i.getIsPrimary());
                image.setDisplayOrder(i.getDisplayOrder() != null ? i.getDisplayOrder() : 0);

                if (Boolean.TRUE.equals(i.getIsPrimary())) {
                    hasPrimary = true;
                }

                image.setProduct(product); // IMPORTANT

                images.add(image);
            }

            // Ensure at least one primary image
            if (!images.isEmpty() && !hasPrimary) {
                images.iterator().next().setPrimary(true);
            }
        }

        product.setVariants(variants);
        product.setImages(images);

        // 6. Save
        Product saved = productRepository.save(product);

        return saved.getId();
    }
}