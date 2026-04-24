package com.praisomart.backend.adminPanel.controller;

import com.praisomart.backend.adminPanel.dto.ProductRequestDTO;
import com.praisomart.backend.adminPanel.service.ProductServiceAdmin;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/products")
public class ProductAdminController {

    private final ProductServiceAdmin productServiceAdmin;

    public ProductAdminController(ProductServiceAdmin productServiceAdmin) {
        this.productServiceAdmin = productServiceAdmin;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {

        Long productId = productServiceAdmin.createProduct(request);

        return ResponseEntity.ok("Product created successfully with ID: " + productId);
    }
}
