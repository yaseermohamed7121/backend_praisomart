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
import com.praisomart.backend.exception.*;

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
        BigDecimal totalAmount = BigDecimal.ZERO.setScale(2);
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
                        break;
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

        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseGet(() -> cartRepository.save(new Cart(userId)));

        ProductVariant variant = productVariantRepository.findById(dto.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        int requestedQty = dto.getQuantity();
        int availableStock = variant.getStock();

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductVariantId(cart.getId(), dto.getProductVariantId())
                .orElse(null);

        int finalQty;

        if (cartItem != null) {

            if (!cartItem.getIsActive()) {
                cartItem.setIsActive(true);

                finalQty = Math.min(requestedQty, availableStock);
                cartItem.setQuantity(finalQty);

            } else {
                int newQty = cartItem.getQuantity() + requestedQty;
                finalQty = Math.min(newQty, availableStock);

                cartItem.setQuantity(finalQty);
            }

        } else {
            finalQty = Math.min(requestedQty, availableStock);

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductVariant(variant);
            cartItem.setQuantity(finalQty);
            cartItem.setPrice(variant.getPrice());
            cartItem.setIsActive(true);
        }

        cartItemRepository.save(cartItem);

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        String message = (requestedQty > availableStock)
                ? "Only " + availableStock + " items available"
                : "Item added successfully";

        return new AddCartItemResponseDTO(
                message,
                cartItem.getId(),
                cartItem.getQuantity()
        );
    }

    // Update item quantity in cart
    @Transactional
    public UpdateCartItemResponseDTO updateItem(Long userId, Long cartItemId, int quantity) {

        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new UnauthorizedException("Unauthorized access");
        }

        if (!cartItem.getIsActive()) {
            throw new BadRequestException("Cart item is inactive");
        }

        ProductVariant variant = cartItem.getProductVariant();
        int availableStock = variant.getStock();

        int finalQty;

        if (quantity > availableStock) {
            finalQty = availableStock;
        } else {
            finalQty = quantity;
        }

        cartItem.setQuantity(finalQty);
        cartItemRepository.save(cartItem);

        BigDecimal itemTotal =
                cartItem.getPrice().multiply(BigDecimal.valueOf(finalQty));

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

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

    // Remove product from cart
    @Transactional
    public RemoveCartItemResponseDTO removeItem(Long userId, Long cartItemId) {

        Cart cart = cartRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new UnauthorizedException("Unauthorized access");
        }

        if (!cartItem.getIsActive()) {
            throw new BadRequestException("Cart item already removed");
        }

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