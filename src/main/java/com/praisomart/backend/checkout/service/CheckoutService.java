package com.praisomart.backend.checkout.service;

import com.praisomart.backend.cart.dto.CartItemResponseDTO;
import com.praisomart.backend.cart.entity.Cart;
import com.praisomart.backend.cart.entity.CartItem;
import com.praisomart.backend.cart.repository.CartItemRepository;
import com.praisomart.backend.cart.repository.CartRepository;
import com.praisomart.backend.address.dto.AddressResponseDTO;
import com.praisomart.backend.address.entity.Address;
import com.praisomart.backend.address.repository.AddressRepository;
import com.praisomart.backend.checkout.dto.CheckoutResponseDTO;
import com.praisomart.backend.products.entity.ProductImage;
import com.praisomart.backend.products.entity.ProductVariant;
import com.praisomart.backend.products.repository.ProductVariantRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository variantRepository;
    private final AddressRepository addressRepository;

    public CheckoutService(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductVariantRepository variantRepository,
                           AddressRepository addressRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.variantRepository = variantRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional // Read-only style transaction
    public CheckoutResponseDTO getCheckout(Long cartId, Long userId) {

        // 🔹 1. Validate Cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized cart access");
        }

        // 🔹 2. Fetch Active Cart Items
        List<CartItem> cartItems =
                cartItemRepository.findByCartIdAndIsActiveTrue(cartId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 🔹 3. Process Items
        List<CartItemResponseDTO> itemDTOs = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            ProductVariant variant = variantRepository
                    .findById(item.getProductVariant().getId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found"));

            if (variant.getStock() == null || variant.getStock() <= 0) {
                throw new RuntimeException(
                        "Out of stock: " + variant.getProduct().getName()
                );
            }

            if (item.getQuantity() > variant.getStock()) {
                throw new RuntimeException(
                        "Only " + variant.getStock() + " items available for "
                                + variant.getProduct().getName()
                );
            }

            BigDecimal price = variant.getPrice();

            if (price == null) {
                throw new RuntimeException("Price not available for product");
            }

            BigDecimal itemTotal =
                    price.multiply(BigDecimal.valueOf(item.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);

            // Image handling
            String imageUrl = getPrimaryImage(variant);

            CartItemResponseDTO dto = new CartItemResponseDTO(
                    item.getId(),
                    variant.getProduct().getId(),
                    variant.getId(),
                    variant.getProduct().getName(),
                    imageUrl,
                    item.getQuantity(),
                    price,
                    itemTotal
            );

            itemDTOs.add(dto);
        }

        // 🔹 4. Fetch Addresses (NO exception → empty list allowed)
        List<Address> addresses =
                addressRepository.findByUserIdAndIsActiveTrueOrderByIsDefaultDesc(userId);

        List<AddressResponseDTO> addressDTOs = new ArrayList<>();

        for (Address addr : addresses) {
            AddressResponseDTO dto = new AddressResponseDTO();

            dto.setId(addr.getId());
            dto.setFullName(addr.getFullName());
            dto.setPhoneNumber(addr.getPhoneNumber());
            dto.setPincode(addr.getPincode());
            dto.setAddressLine1(addr.getAddressLine1());
            dto.setAddressLine2(addr.getAddressLine2());
            dto.setCity(addr.getCity());
            dto.setState(addr.getState());
            dto.setIsDefault(addr.getIsDefault());

            addressDTOs.add(dto);
        }

        // 🔹 5. Final Response
        return new CheckoutResponseDTO(
                cartId,
                itemDTOs,
                totalAmount,
                addressDTOs
        );
    }

    // Helper method (clean code)
    private String getPrimaryImage(ProductVariant variant) {
        if (variant.getProduct().getImages() != null &&
                !variant.getProduct().getImages().isEmpty()) {

            for (ProductImage img : variant.getProduct().getImages()) {
                if (Boolean.TRUE.equals(img.getPrimary())) {
                    return img.getImageUrl();
                }
            }

            return variant.getProduct()
                    .getImages()
                    .iterator()
                    .next()
                    .getImageUrl();
        }
        return null;
    }
}