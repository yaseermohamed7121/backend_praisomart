package com.praisomart.backend.cart.service;

import com.praisomart.backend.cart.dto.*;
import com.praisomart.backend.cart.entity.Cart;
import com.praisomart.backend.cart.entity.CartItem;
import com.praisomart.backend.cart.repository.CartItemRepository;
import com.praisomart.backend.cart.repository.CartRepository;
import com.praisomart.backend.products.entity.Product;
import com.praisomart.backend.products.entity.ProductImage;
import com.praisomart.backend.products.entity.ProductVariant;
import com.praisomart.backend.products.repository.ProductVariantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductVariantRepository productVariantRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariantRepository = productVariantRepository;
    }

    // Fetch Cart of Logged-in User
    public CartResponseDTO getCart(Long userId) {

        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElse(null);

        if (cart == null) {
            return new CartResponseDTO(); // empty cart
        }

        List<CartItemResponseDTO> itemDTOList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO.setScale(2);;
        int totalItems = 0;

        if (cart.getItems() == null) {
            return new CartResponseDTO(cart.getId(), new ArrayList<>(), 0, BigDecimal.ZERO);
        }

        for (CartItem item : cart.getItems()) {

            if (!item.getIsActive()) continue;

            ProductVariant variant = item.getProductVariant();
            Product product = variant.getProduct();

            CartItemResponseDTO dto = new CartItemResponseDTO();

            dto.setCartItemId(item.getId());
            dto.setProductId(product.getId());
            dto.setProductVariantId(variant.getId());
            dto.setProductName(product.getName());

            String imageUrl = null;

            Set<ProductImage> images = product.getImages();

            if (images != null && !images.isEmpty()) {

                for (ProductImage img : images) {
                    if (img.getPrimary() != null && img.getPrimary()) {
                        imageUrl = img.getImageUrl();
                        break;
                    }
                }

                if (imageUrl == null) {
                    for (ProductImage img : images) {
                        imageUrl = img.getImageUrl();
                        break; // take first image
                    }
                }
            }

            dto.setImageUrl(imageUrl);

            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());

            BigDecimal itemTotal =
                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            dto.setTotalPrice(itemTotal);

            totalAmount = totalAmount.add(itemTotal);
            totalItems += item.getQuantity();

            itemDTOList.add(dto);
        }

        CartResponseDTO response = new CartResponseDTO();
        response.setCartId(cart.getId());
        response.setItems(itemDTOList);
        response.setTotalAmount(totalAmount);
        response.setTotalItems(totalItems);

        return response;
    }

    // Add products to cart
    @Transactional
    public AddCartItemResponseDTO addItem(Long userId, AddCartItemRequestDTO dto) {

        // Get or create cart
        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(userId);
                    return cartRepository.save(newCart);
                });

        // Get product variant
        ProductVariant variant = productVariantRepository.findById(dto.getProductVariantId())
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        int requestedQty = dto.getQuantity();
        int availableStock = variant.getStock();

        // Check existing cart item
        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductVariantIdAndIsActiveTrue(cart.getId(), dto.getProductVariantId())
                .orElse(null);

        int finalQty;

        if (cartItem != null) {
            int newQty = cartItem.getQuantity() + requestedQty;

            if (newQty > availableStock) {
                finalQty = availableStock;
            } else {
                finalQty = newQty;
            }

            cartItem.setQuantity(finalQty);

        } else {
            if (requestedQty > availableStock) {
                finalQty = availableStock;
            } else {
                finalQty = requestedQty;
            }

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductVariant(variant);
            cartItem.setQuantity(finalQty);
            cartItem.setPrice(variant.getPrice());
        }

        cartItemRepository.save(cartItem);

        // update cart time
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        // message handling
        String message = (requestedQty > availableStock)
                ? "Only " + availableStock + " items available"
                : "Item added successfully";

        return new AddCartItemResponseDTO(
                message,
                cartItem.getId(),
                cartItem.getQuantity()
        );
    }

    @Transactional
    public UpdateCartItemResponseDTO updateItem(Long userId, Long cartItemId, int quantity) {

        // Get cart
        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Get item
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // SECURITY CHECK (CRITICAL)
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        if (!cartItem.getIsActive()) {
            throw new RuntimeException("Cart item is inactive");
        }

        //  STOCK CHECK
        ProductVariant variant = cartItem.getProductVariant();
        int availableStock = variant.getStock();

        int finalQty;

        if (quantity > availableStock) {
            finalQty = availableStock;
        } else {
            finalQty = quantity;
        }

        // Update quantity
        cartItem.setQuantity(finalQty);
        cartItemRepository.save(cartItem);

        // Calculate total
        BigDecimal itemTotal =
                cartItem.getPrice().multiply(BigDecimal.valueOf(finalQty));

        // Update cart timestamp
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        // Message
        String message = (quantity > availableStock)
                ? "Only " + availableStock + " items available"
                : "Cart item updated successfully";

        return new UpdateCartItemResponseDTO(
                message,
                cartItem.getId(),
                cartItem.getQuantity(),
                itemTotal
        );
    }

    @Transactional
    public RemoveCartItemResponseDTO removeItem(Long userId, Long cartItemId) {

        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        if (!cartItem.getIsActive()) {
            throw new RuntimeException("Cart item already removed");
        }

        // soft delete
        cartItem.setIsActive(false);
        cartItemRepository.save(cartItem);

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        return new RemoveCartItemResponseDTO(
                "Item removed successfully",
                cartItemId
        );
    }
}