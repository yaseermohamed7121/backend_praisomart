package com.praisomart.backend.wishlist.repository;

import com.praisomart.backend.auth.entity.User;
import com.praisomart.backend.products.entity.Product;
import com.praisomart.backend.wishlist.entity.WishlistEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {

    Optional<WishlistEntity> findByUserAndProduct(User user, Product product);

    List<WishlistEntity> findByUserAndIsActiveTrue(User user);

    boolean existsByUserAndProductAndIsActiveTrue(User user, Product product);

    // ✅ ADD THIS (IMPORTANT)
    @Query("""
           SELECT w.product.id 
           FROM WishlistEntity w 
           WHERE w.user.id = :userId 
           AND w.isActive = true
           """)
    List<Long> findProductIdsByUser(@Param("userId") Long userId);
}