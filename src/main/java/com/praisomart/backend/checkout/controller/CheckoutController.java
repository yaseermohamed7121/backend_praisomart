package com.praisomart.backend.checkout.controller;

import com.praisomart.backend.auth.security.CustomerUserDetails;
import com.praisomart.backend.checkout.dto.CheckoutResponseDTO;
import com.praisomart.backend.checkout.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @GetMapping("/{cartId}")
    public ResponseEntity<CheckoutResponseDTO> getCheckout(
            @PathVariable Long cartId,
            @AuthenticationPrincipal CustomerUserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("User not authenticated");
        }

        CheckoutResponseDTO response =
                checkoutService.getCheckout(cartId, userDetails.getUserId());

        return ResponseEntity.ok(response);
    }
}