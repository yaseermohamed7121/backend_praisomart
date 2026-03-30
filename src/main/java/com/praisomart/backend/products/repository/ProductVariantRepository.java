package com.praisomart.backend.products.repository;

import com.praisomart.backend.products.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant,Long> {

    Optional<ProductVariant> findById(Long id);
}
