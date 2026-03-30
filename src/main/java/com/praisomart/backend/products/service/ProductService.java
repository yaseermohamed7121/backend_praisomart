package com.praisomart.backend.products.service;

import com.praisomart.backend.products.dto.ProductDetailResponse;
import com.praisomart.backend.products.dto.ProductListResponse;
import com.praisomart.backend.products.dto.VariantResponse;
import com.praisomart.backend.products.entity.Product;
import com.praisomart.backend.products.entity.ProductImage;
import com.praisomart.backend.products.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    // PRODUCT LIST (Category + Explore All)
    public List<ProductListResponse> getProducts(Long categoryId) {


        return productRepository.findProducts(categoryId)
                .stream()
                .map(p -> {

                    // FIXED HERE (getPrimary())
                    String image = p.getImages().stream()
                            .filter(img -> Boolean.TRUE.equals(img.getPrimary()))
                            .findFirst()
                            .map(ProductImage::getImageUrl)
                            .orElse(null);

                    Double price = p.getVariants().stream()
                            .findFirst()
                            .map(v -> v.getPrice().doubleValue())
                            .orElse(0.0);

                    Boolean inStock = p.getVariants().stream()
                            .anyMatch(v -> v.getStock() > 0);

                    return new ProductListResponse(
                            p.getId(),
                            p.getName(),
                            p.getColor(),
                            image,
                            price,
                            inStock
                    );
                }).toList();
    }

    // PRODUCT DETAIL API
    public ProductDetailResponse getProductDetail(Long id) {

        Product p = productRepository.findProductDetail(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<String> images = p.getImages()
                .stream()
                .map(ProductImage::getImageUrl)
                .toList();

        List<VariantResponse> variants = p.getVariants()
                .stream()
                .map(v -> new VariantResponse(
                        v.getSize(),
                        v.getPrice().doubleValue(),
                        v.getStock()
                ))
                .toList();

        return new ProductDetailResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getColor(),
                images,
                variants
        );
    }

}