package com.praisomart.backend.products.repository;

import com.praisomart.backend.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT DISTINCT p FROM Product p
        LEFT JOIN FETCH p.images i
        LEFT JOIN FETCH p.variants v
        WHERE p.isActive = true
        AND (:categoryId IS NULL OR p.category.id = :categoryId)
    """)
    List<Product> findProducts(Long categoryId);

    @Query("""
    SELECT p FROM Product p
    LEFT JOIN FETCH p.images
    LEFT JOIN FETCH p.variants
    WHERE p.id = :id
""")
    Optional<Product> findProductDetail(Long id);
}
