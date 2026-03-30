package com.praisomart.backend.products.controller;

import com.praisomart.backend.products.dto.ProductDetailResponse;
import com.praisomart.backend.products.dto.ProductListResponse;
import com.praisomart.backend.products.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService=productService;
    }

    // Category filter + Explore All
    @GetMapping
    public ResponseEntity<List<ProductListResponse>> getProducts(
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(productService.getProducts(categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDetail(id));
    }
}