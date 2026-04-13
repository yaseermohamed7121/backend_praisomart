package com.praisomart.backend.cart.repository;

import com.praisomart.backend.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Find item by cart + product variant
    Optional<CartItem> findByCartIdAndProductVariantIdAndIsActiveTrue(
            Long cartId,
            Long productVariantId
    );

    Optional<CartItem> findByCartIdAndProductVariantId(
            Long cartId,
            Long productVariantId
    );

    List<CartItem> findByCartIdAndIsActiveTrue(Long cartId);

}